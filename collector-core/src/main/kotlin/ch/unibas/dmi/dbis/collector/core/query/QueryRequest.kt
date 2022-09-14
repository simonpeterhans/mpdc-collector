package ch.unibas.dmi.dbis.collector.core.query

import ch.unibas.dmi.dbis.collector.core.Collector
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import mu.KotlinLogging
import retrofit2.Call
import java.io.IOException
import java.io.InterruptedIOException
import java.nio.file.Files
import java.nio.file.Path
import java.time.Instant
import kotlin.io.path.absolute

private val logger = KotlinLogging.logger {}

private val JSON_PATH: Path = Collector.dataPathPrefix.resolve("json")

// TODO Consider replacing this with just a StreamStatus.
enum class FetchStatus {

    INTERRUPT,
    REQUEST_ERROR,
    RESPONSE_ERROR,
    INVALID_JSON,
    RAW_PARSING_ERROR,
    SUCCESS

}

data class RequestResultWithStatus<T>(
    val status: FetchStatus,
    val statusText: String = "",
    val response: T? = null
)

fun <T> executeRequest(
    req: Call<JsonNode>,
    resultClass: Class<T>,
    mapper: ObjectMapper,
    query: SubQuery
) = executeRequest(req, resultClass, mapper, query.label)

fun <T> executeRequest(
    req: Call<JsonNode>,
    resultClass: Class<T>,
    mapper: ObjectMapper,
    requestLabel: String
): RequestResultWithStatus<T> {
    val res = try {
        req.execute()
    } catch (ie: InterruptedIOException) {
        // Interrupt.
        logger.info {
            "Request for $requestLabel failed. Reason: Interrupt."
        }
        throw InterruptedException()
//        return RequestResultWithStatus(FetchStatus.INTERRUPT, "Interrupt.")
    } catch (io: IOException) {
        // Socket timeout etc.
        logger.error {
            "Request for $requestLabel failed. Reason: I/O Exception."
        }
        return RequestResultWithStatus(FetchStatus.REQUEST_ERROR, "I/O exception.")
    } catch (rt: RuntimeException) {
        /*
         * Since we're decoding the raw JSON later, this can only happen if the returned JSON string
         * is invalid.
         * There's nothing we can really do here, because if we can't even parse the response as a
         * JSON object we can't look at the response body.
         * If this ever becomes necessary, add an interceptor that checks for JSON validity.
         */
        logger.error {
            "Request for $requestLabel failed. Reason: Response is not a valid JSON object."
        }
        return RequestResultWithStatus(FetchStatus.INVALID_JSON, "Invalid response JSON.")
    }

    // Store response for debugging.
    val s = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(res.body())

    // Create path if not exists.
    val path = JSON_PATH.resolve(requestLabel)
    Files.createDirectories(path.absolute())
    path.resolve("${Instant.now()}.json").toFile().printWriter().use {
        it.println(s)
    }

    // Request executed, check if it was successful.
    if (!res.isSuccessful) {
        logger.error {
            "Request for $requestLabel returned with non-200 response!\n" +
                "Error body: ${res.errorBody()?.string()}"
        }
        logger.error { }
        return RequestResultWithStatus(
            FetchStatus.RESPONSE_ERROR,
            "Error code ${res.code()} in HTTP response."
        )
    }

    // Try to parse body into the object we actually want.
    val body = try {
        mapper.treeToValue(res.body(), resultClass)
    } catch (e: Exception) {
        logger.error {
            "Failed to parse response for request for $requestLabel!\n" +
                "Cause: ${e.cause}\n" +
                "JSON body: ${res.body()}"
        }
        return RequestResultWithStatus(
            FetchStatus.RAW_PARSING_ERROR,
            "JSON parsing error for ${resultClass.name}."
        )
    }

    return RequestResultWithStatus(FetchStatus.SUCCESS, "", body)
}
