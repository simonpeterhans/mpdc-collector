package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter

interface TwitterQueryParametrizable {

    val type: TwitterQueryParameterType

    fun getRaw(): String

    fun toQueryParameter(): String

}
