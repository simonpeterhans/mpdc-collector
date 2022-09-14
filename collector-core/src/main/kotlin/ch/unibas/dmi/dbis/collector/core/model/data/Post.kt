package ch.unibas.dmi.dbis.collector.core.model.data

import ch.unibas.dmi.dbis.collector.core.model.flow.EntityType
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant

enum class RelationType {

    COMMENT,
    QUOTE,
    QUOTE_COMMENT

}

data class PostUrl(
    val url: String
)

data class PostRelation(
    val idFrom: String,
    val idTo: String,
    val relType: RelationType
)

data class PostTextualContent(
    val title: String?,
    val text: String?,
    val language: String?
)

data class Post(
    val platformId: String,
    val authorPlatformId: String?,
    val authorStatus: AuthorStatus?,
    val texts: List<PostTextualContent>,
    val urls: List<PostUrl>,
    val rels: List<PostRelation>,
    val mos: List<MultimediaObject>,
    override val raw: JsonNode,
    override val platform: Platform,
    override val platformTimestamp: Instant,
    override val fetchTimestamp: Instant = Instant.now(),
    override val apiString: String,
    override val id: Long? = null
) : ModelEntity(raw, platform, platformTimestamp, fetchTimestamp, apiString) {

    override val type: EntityType = EntityType.POST

}
