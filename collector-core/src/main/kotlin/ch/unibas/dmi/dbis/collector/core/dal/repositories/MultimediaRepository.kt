package ch.unibas.dmi.dbis.collector.core.dal.repositories

import ch.unibas.dmi.dbis.collector.core.model.data.MediaType
import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform

object MultimediaRepository : Repository<IMultimediaRepository>(), IMultimediaRepository {

    override fun updateMultimediaResource(u: MultimediaResourceUpdate): Boolean {
        return impl.updateMultimediaResource(u)
    }

    override fun getDetailsByUrl(url: String): MultimediaDetails? {
        return impl.getDetailsByUrl(url)
    }

    override fun getMultimediaObjectsForProcessing(
        platform: ApiPlatform,
        mediaTypes: List<MediaType>,
        numResults: Int
    ): List<MultimediaProcessingRequest> {
        return impl.getMultimediaObjectsForProcessing(
            platform,
            mediaTypes,
            numResults
        )
    }

}
