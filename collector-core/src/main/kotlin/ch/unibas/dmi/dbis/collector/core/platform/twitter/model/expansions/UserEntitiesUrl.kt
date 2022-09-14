package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.UrlEntity
import com.fasterxml.jackson.annotation.JsonProperty

data class UserEntitiesUrl(
    @JsonProperty("urls")
    val urls: List<UrlEntity>
)

