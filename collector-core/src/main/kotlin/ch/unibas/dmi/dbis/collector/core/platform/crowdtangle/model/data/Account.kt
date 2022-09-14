package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.data

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * CrowdTangle account data model.
 *
 * @param accountType The account type, for Facebook only.
 * @param handle The handle or vanity URL of the account.
 * @param id The unique identifier of the account in the CrowdTangle system. This ID is specific to CrowdTangle, not the platform on which the account exists.
 * @param name The name of the page, group, or profile.
 * @param pageAdminTopCountry The ISO country code of the country from where the plurality of page administrators operate.
 * @param pageCategory The page category as submitted by the page.
 * @param pageCreatedDate Represents a date string of CrowdTangle. Note that these are just strings, because CrowdTangle does not adhere to any ISO standard.
 * @param pageDescription The description of the page as documented in Page Transparency information.
 * @param platform
 * @param platformId The platform's ID for the account. This is not shown for Facebook public users.
 * @param profileImage A URL pointing at the profile image.
 * @param subscriberCount The number of subscribers/likes/followers the account has. By default, the subscriberCount property will show page Followers. You can select either Page Likes or Followers in your Dashboard settings.
 * @param url A link to the account on its platform.
 * @param verified Whether the account is verified by the platform, if supported by the platform. If not supported, will return false.
 */
data class Account(
    @JsonProperty("accountType")
    val accountType: AccountType? = null,

    @JsonProperty("handle")
    val handle: String? = null,

    @JsonProperty("id")
    val id: Int? = null,

    @JsonProperty("name")
    val name: String? = null,

    @JsonProperty("pageAdminTopCountry")
    val pageAdminTopCountry: String? = null,

    @JsonProperty("pageCategory")
    val pageCategory: String? = null,

    @JsonProperty("pageCreatedDate")
    val pageCreatedDate: String? = null,

    @JsonProperty("pageDescription")
    val pageDescription: String? = null,

    @JsonProperty("platform")
    val platform: Platform? = null,

    @JsonProperty("platformId")
    val platformId: String? = null,

    @JsonProperty("profileImage")
    val profileImage: String? = null,

    @JsonProperty("subscriberCount")
    val subscriberCount: Int? = null,

    @JsonProperty("url")
    val url: String? = null,

    @JsonProperty("verified")
    val verified: Boolean? = null
)
