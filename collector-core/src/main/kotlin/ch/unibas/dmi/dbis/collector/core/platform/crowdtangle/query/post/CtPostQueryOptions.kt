package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query.post

interface CtPostQueryOptions {

    val keywords: List<String>
    val queryDelayMinutes: Int
    val includeHistory: Boolean

}

data class CtPostQueryOptionsData(
    override val keywords: List<String>,
    override val queryDelayMinutes: Int,
    override val includeHistory: Boolean
) : CtPostQueryOptions {

    val keywordString: String? = if (keywords.isEmpty()) {
        null
    } else {
        keywords.joinToString(",") { it }
    }

}
