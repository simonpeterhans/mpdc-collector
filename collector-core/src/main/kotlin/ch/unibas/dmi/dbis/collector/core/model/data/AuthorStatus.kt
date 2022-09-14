package ch.unibas.dmi.dbis.collector.core.model.data

import ch.unibas.dmi.dbis.collector.core.model.flow.EntityType
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant

data class AuthorStatus(
    val platformId: String?,
    val name: String,
    val mos: List<MultimediaObject>,
    override val raw: JsonNode,
    override val platform: Platform,
    override val platformTimestamp: Instant,
    override val fetchTimestamp: Instant,
    override val apiString: String,
    override val id: Long? = null
) : ModelEntity(raw, platform, platformTimestamp, fetchTimestamp, apiString) {

    override val type: EntityType = EntityType.AUTHOR_STATE

}
