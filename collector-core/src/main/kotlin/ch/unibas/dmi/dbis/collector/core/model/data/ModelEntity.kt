package ch.unibas.dmi.dbis.collector.core.model.data

import ch.unibas.dmi.dbis.collector.core.model.Identifiable
import ch.unibas.dmi.dbis.collector.core.model.flow.EntityType
import ch.unibas.dmi.dbis.collector.core.model.platform.Platform
import com.fasterxml.jackson.databind.JsonNode
import java.time.Instant

abstract class ModelEntity(
    open val raw: JsonNode,
    open val platform: Platform,
    open val platformTimestamp: Instant,
    open val fetchTimestamp: Instant,
    open val apiString: String
) : Identifiable<Long> {

    abstract val type: EntityType

}
