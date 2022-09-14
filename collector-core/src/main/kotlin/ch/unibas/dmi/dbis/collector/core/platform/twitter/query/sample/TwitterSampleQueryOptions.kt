package ch.unibas.dmi.dbis.collector.core.platform.twitter.query.sample


interface TwitterSampleQueryOptions {

    val referencedTweetsDepth: Int

}

data class TwitterSampleQueryOptionsData(
    override val referencedTweetsDepth: Int
) : TwitterSampleQueryOptions
