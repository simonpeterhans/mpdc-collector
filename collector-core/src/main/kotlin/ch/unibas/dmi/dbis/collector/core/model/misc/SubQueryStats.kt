package ch.unibas.dmi.dbis.collector.core.model.misc

data class SubQueryStats(
    val numPosts: Long,
    val numAuthorStatuses: Long,
    val numTotalMediaObjects: Long,
    val numProcessedMediaObjects: Long,
    val numUniqueMediaObjects: Long,
    val numRemainingMediaObjects: Long
)
