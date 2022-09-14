package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter

class TwitterKeywordQueryParameter(
    val keyword: String
) : TwitterQueryParametrizable {

    override val type = TwitterQueryParameterType.KEYWORD

    override fun getRaw(): String = keyword

    override fun toQueryParameter(): String = "\"$keyword\""

}
