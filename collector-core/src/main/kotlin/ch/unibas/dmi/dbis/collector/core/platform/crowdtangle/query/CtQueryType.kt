package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.query

import ch.unibas.dmi.dbis.collector.core.model.query.PlatformQueryType

enum class CtQueryType(private val recoverable: Boolean) : PlatformQueryType {

    CT_POST_LIST_QUERY(true),
    CT_POST_SEARCH_QUERY(true);

    override fun isRecoverable(): Boolean = recoverable

}
