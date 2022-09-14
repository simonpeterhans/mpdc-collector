package ch.unibas.dmi.dbis.collector.core.platform.crowdtangle.model.responses

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Contains references to the previous and next page of query results. Note that this includes the API token in the URL.
 *
 * @param nextPage The next page of query results (empty if the last page of query results has been reached).
 * @param previousPage The previous page of query results (empty if the current request represents the first page of query results).
 */
data class Pagination(
    @JsonProperty("nextPage")
    val nextPage: String? = null,

    @JsonProperty("previousPage")
    val previousPage: String? = null
)
