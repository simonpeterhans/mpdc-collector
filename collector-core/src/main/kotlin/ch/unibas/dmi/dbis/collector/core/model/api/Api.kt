package ch.unibas.dmi.dbis.collector.core.model.api

import ch.unibas.dmi.dbis.collector.core.model.platform.ApiPlatform
import ch.unibas.dmi.dbis.collector.core.model.query.SubQuery
import ch.unibas.dmi.dbis.collector.core.query.QueryRunner
import ch.unibas.dmi.dbis.collector.core.util.Versioned

// Versioning: Should be updated if the endpoint's data models change!
interface Api<T : SubQuery> : Versioned {

    val platform: ApiPlatform

    fun label(): String = "$platform-$version"

    fun processSubQuery(subQuery: T): QueryRunner

}
