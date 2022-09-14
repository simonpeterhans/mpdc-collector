package ch.unibas.dmi.dbis.collector.core.processing

import ch.unibas.dmi.dbis.cineast.client.apis.SessionApi
import ch.unibas.dmi.dbis.cineast.client.models.ExtractionContainerMessage
import ch.unibas.dmi.dbis.cineast.client.models.ExtractionItemContainer
import ch.unibas.dmi.dbis.cineast.client.models.MediaObjectDescriptor
import ch.unibas.dmi.dbis.collector.core.Collector
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaProcessingRequest
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaRepository
import ch.unibas.dmi.dbis.collector.core.dal.repositories.MultimediaResourceUpdate
import ch.unibas.dmi.dbis.collector.core.model.data.ResourceStatus
import mu.KotlinLogging
import okhttp3.*
import okio.HashingSink
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import java.util.*
import kotlin.io.path.absolute

private val logger = KotlinLogging.logger {}

class MultimediaProcessor(
    private val client: OkHttpClient,
    cineastUrl: String
) {

    companion object {

        val TMP_PATH: Path = Collector.dataPathPrefix.resolve("tmp")
        var cineastStarted = false

        private const val UNKNOWN_TYPE = "unknown"

        init {
            Files.createDirectories(TMP_PATH.absolute())
        }

    }

    private val cineastSession = SessionApi(cineastUrl)

    private fun updateMultimedia(
        id: Long,
        resourceStatus: ResourceStatus,
        hash: String? = null,
        contentType: String? = null
    ) {
        MultimediaRepository.updateMultimediaResource(
            MultimediaResourceUpdate(id, hash, contentType, resourceStatus, Instant.now())
        )
    }

    private fun additionalProcessing(f: File, indexInCineast: Boolean) {
        if (indexInCineast) {
            if (!cineastStarted) {
                try {
                    cineastSession.startExtraction()
                    cineastStarted = true
                } catch (e: Exception) {
                    // TODO Handle this.
                }
            }

            cineastSession.extractItem(
                ExtractionContainerMessage(
                    listOf(
                        ExtractionItemContainer(
                            MediaObjectDescriptor(
                                objectid = null,
                                name = "",
                                path = f.absolutePath.toString(),
                                mediatype = null,
                                exists = false,
                                contentURL = ""
                            ),
                            null,
                            f.toURI().toString()
                        )
                    )
                )
            )
        }
    }

    private fun processResponse(
        id: Long,
        body: ResponseBody,
        collection: String,
        indexInCineast: Boolean
    ) {
        val contentType = body.contentType()?.subtype ?: UNKNOWN_TYPE
        var hash: String?
        val tmpId = UUID.randomUUID().toString()
        val tmpFile = TMP_PATH.resolve(tmpId).toFile()

        val file = tmpFile.sink().buffer().use { bufferedSink ->
            HashingSink.md5(bufferedSink).use { hashingSink ->
                hashingSink.buffer().use { sink ->
                    body.source().use { src ->
                        sink.writeAll(src)
                    }
                }

                hash = hashingSink.hash.hex()

                val newFile =
                    Collector.dataPathPrefix
                        .resolve(collection)
                        .resolve("$hash.$contentType")
                        .toFile()
                newFile.parentFile.mkdirs()

                tmpFile.renameTo(newFile)

                newFile
            }
        }

        if (file != null) {
            additionalProcessing(file, indexInCineast)
        }

        updateMultimedia(id, ResourceStatus.STORED, hash, contentType)
    }

    private fun downloadMultimedia(
        id: Long,
        url: String,
        collection: String,
        indexInCineast: Boolean
    ) {
        val req = Request.Builder().url(url).build()

        client.newCall(req).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    if (response.body != null) {
                        try {
                            // Received response body, try to parse.
                            processResponse(id, response.body!!, collection, indexInCineast)
                        } catch (e: Exception) {
                            // TODO More specific catch here (and not just closed connection).
                            updateMultimedia(id, ResourceStatus.CONNECTION_CLOSED)
                        }
                    } else {
                        // Null body, something went wrong.
                        updateMultimedia(id, ResourceStatus.INVALID_RESOURCE)
                    }
                } else {
                    // Response did not return with 200.
                    updateMultimedia(id, ResourceStatus.RESOURCE_NOT_FOUND)
                }

                response.close()
            }

            override fun onFailure(call: Call, e: IOException) {
                // Something went wrong while trying to access the resource.
                updateMultimedia(id, ResourceStatus.IO_ERROR)
            }
        })
    }

    fun processMultimedia(mpr: MultimediaProcessingRequest) {
        if (mpr.url == null) {
            updateMultimedia(mpr.id, ResourceStatus.UNKNOWN_RESOURCE)
        } else {
            // Try to look up, so we don't have to fetch it twice.
            val res = MultimediaRepository.getDetailsByUrl(mpr.url)

            if (res == null) {
                // Not found, download.
                downloadMultimedia(mpr.id, mpr.url, mpr.collection, mpr.indexInCineast)
            } else {
                // Already obtained previously, don't download again.
                updateMultimedia(mpr.id, res.status, res.hash, res.contentType)
            }
        }
    }

}
