package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.parameter

data class TwitterAccountQueryParameter(
    val account: String
) : TwitterQueryParametrizable {

    override val type = TwitterQueryParameterType.ACCOUNT

    override fun getRaw(): String = account

    override fun toQueryParameter(): String = "from:$account"

}
