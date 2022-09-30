package ch.unibas.dmi.dbis.collector.core.platform.twitter.api.endpoints

object TwitterApiOptions {

    val allExpansions = setOf(
        "attachments.poll_ids",
        "attachments.media_keys",
        "author_id",
        "edit_history_tweet_ids",
        "entities.mentions.username",
        "geo.place_id",
        "in_reply_to_user_id",
        "referenced_tweets.id",
        "referenced_tweets.id.author_id"
    ).toList().joinToString(",") { it }

    val allTweetFields = setOf(
        "attachments",
        "author_id",
        "context_annotations",
        "conversation_id",
        "created_at",
        "entities",
        "geo",
        "id",
        "in_reply_to_user_id",
        "lang",
        "possibly_sensitive",
        "public_metrics",
//        "non_public_metrics", // User search only.
//        "organic_metrics", // User search only.
//        "promoted metrics", // User search only.
        "referenced_tweets",
        "reply_settings",
        "source",
        "text",
        "withheld"
    ).toList().joinToString(",") { it }

    val allUserFields = setOf(
        "created_at",
        "description",
        "entities",
        "id",
        "location",
        "name",
        "pinned_tweet_id",
        "profile_image_url",
        "protected",
        "public_metrics",
        "url",
        "username",
        "verified",
        "withheld"
    ).toList().joinToString(",") { it }

    val allMediaFields = setOf(
        "alt_text",
        "duration_ms",
        "height",
        "media_key",
        "preview_image_url",
        "public_metrics",
        "type",
        "url",
        "variants",
        "width"
    ).toList().joinToString(",") { it }

    val allPlaceFields = setOf(
        "contained_within",
        "country",
        "country_code",
        "full_name",
        "geo",
        "id",
        "name",
        "place_type"
    ).toList().joinToString(",") { it }

    val allPollFields = setOf(
        "duration_minutes",
        "end_datetime",
        "id",
        "options",
        "voting_status",
    ).toList().joinToString(",") { it }

}
