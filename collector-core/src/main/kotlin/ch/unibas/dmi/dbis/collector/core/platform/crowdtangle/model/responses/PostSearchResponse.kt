package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Response message for HTTP code 200 for a CrowdTangle archive search.
 * Responses:
 *  - 200: OK
 *  - 400: Bad Request (invalid parameter)
 *  - 401: Unauthorized (invalid API token)
 *  - 403: Forbidden (invalid list ID)
 *
 * @param status The HTTP status code of the request.
 * @param result The actual result of the request; contains a list of posts if the request was successful.
 * @param code The HTTP response code (provided by CrowdTangle) of the request (only for non-200 responses).
 * @param message: A response string describing the error (only for non-200 responses).
 *
 */
data class PostSearchResponse(
    @JsonProperty("status")
    val status: Int? = null,

    @JsonProperty("result")
    val result: PostSearchResult? = null,

    @JsonProperty("code")
    val code: Int? = null, // Only set upon error.

    @JsonProperty("message")
    val message: String? = null // Only set upon error.
)
