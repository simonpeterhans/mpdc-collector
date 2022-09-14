package ch.unibas.dmi.dbis.collector.core.platform.twitter.model.expansions

import ch.unibas.dmi.dbis.collector.core.platform.twitter.model.tweet.FullTextEntities
import com.fasterxml.jackson.annotation.JsonProperty

data class UserEntities(
    @JsonProperty("url")
    val url: UserEntitiesUrl? = null,

    @JsonProperty("description")
    val description: FullTextEntities? = null
)
