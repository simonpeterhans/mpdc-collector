package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.list

interface CtListQueryOptions {

    val listIds: List<Long>
    val queryDelayMinutes: Int
    val includeHistory: Boolean

}

data class CtListQueryOptionsData(
    override val listIds: List<Long>,
    override val queryDelayMinutes: Int,
    override val includeHistory: Boolean
) : CtListQueryOptions
