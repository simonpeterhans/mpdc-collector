/**
 * Cineast RESTful API
 *
 * Cineast is vitrivr's content-based multimedia retrieval engine. This is it's RESTful API.
 *
 * The version of the OpenAPI document: v1
 * Contact: contact@vitrivr.org
 *
 * Please note:
 * This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * Do not edit this file manually.
 */

@file:Suppress(
    "ArrayInDataClass",
    "EnumEntryName",
    "RemoveRedundantQualifierName",
    "UnusedImport"
)

package ch.unibas.dmi.dbis.cineast.client.apis

import java.io.IOException
import okhttp3.OkHttpClient

import ch.unibas.dmi.dbis.cineast.client.models.ColumnSpecification
import ch.unibas.dmi.dbis.cineast.client.models.DistinctElementsResult
import ch.unibas.dmi.dbis.cineast.client.models.IntegerMessage
import ch.unibas.dmi.dbis.cineast.client.models.SelectResult
import ch.unibas.dmi.dbis.cineast.client.models.SelectSpecification

import com.fasterxml.jackson.annotation.JsonProperty

import ch.unibas.dmi.dbis.cineast.client.infrastructure.ApiClient
import ch.unibas.dmi.dbis.cineast.client.infrastructure.ApiResponse
import ch.unibas.dmi.dbis.cineast.client.infrastructure.ClientException
import ch.unibas.dmi.dbis.cineast.client.infrastructure.ClientError
import ch.unibas.dmi.dbis.cineast.client.infrastructure.ServerException
import ch.unibas.dmi.dbis.cineast.client.infrastructure.ServerError
import ch.unibas.dmi.dbis.cineast.client.infrastructure.MultiValueMap
import ch.unibas.dmi.dbis.cineast.client.infrastructure.PartConfig
import ch.unibas.dmi.dbis.cineast.client.infrastructure.RequestConfig
import ch.unibas.dmi.dbis.cineast.client.infrastructure.RequestMethod
import ch.unibas.dmi.dbis.cineast.client.infrastructure.ResponseType
import ch.unibas.dmi.dbis.cineast.client.infrastructure.Success
import ch.unibas.dmi.dbis.cineast.client.infrastructure.toMultiValue

class MiscApi(basePath: kotlin.String = defaultBasePath, client: OkHttpClient = ApiClient.defaultClient) : ApiClient(basePath, client) {
    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(ApiClient.baseUrlKey, "http://localhost")
        }
    }

    /**
     * Count objects
     * Equivalent to calling SELECT count(*) FROM table. Used to determined #pages for pagination in a frontend or statistical purposes
     * @param table 
     * @return IntegerMessage
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun countRows(table: kotlin.String) : IntegerMessage {
        val localVarResponse = countRowsWithHttpInfo(table = table)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as IntegerMessage
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * Count objects
     * Equivalent to calling SELECT count(*) FROM table. Used to determined #pages for pagination in a frontend or statistical purposes
     * @param table 
     * @return ApiResponse<IntegerMessage?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun countRowsWithHttpInfo(table: kotlin.String) : ApiResponse<IntegerMessage?> {
        val localVariableConfig = countRowsRequestConfig(table = table)

        return request<Unit, IntegerMessage>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation countRows
     *
     * @param table 
     * @return RequestConfig
     */
    fun countRowsRequestConfig(table: kotlin.String) : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/api/v1/count/table/{table}".replace("{"+"table"+"}", table.toString()),
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

    /**
     * Find all distinct elements of a given column
     * Find all distinct elements of a given column. Please note that this operation does cache results.
     * @param columnSpecification  (optional)
     * @return DistinctElementsResult
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun findDistinctElementsByColumn(columnSpecification: ColumnSpecification? = null) : DistinctElementsResult {
        val localVarResponse = findDistinctElementsByColumnWithHttpInfo(columnSpecification = columnSpecification)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as DistinctElementsResult
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * Find all distinct elements of a given column
     * Find all distinct elements of a given column. Please note that this operation does cache results.
     * @param columnSpecification  (optional)
     * @return ApiResponse<DistinctElementsResult?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun findDistinctElementsByColumnWithHttpInfo(columnSpecification: ColumnSpecification?) : ApiResponse<DistinctElementsResult?> {
        val localVariableConfig = findDistinctElementsByColumnRequestConfig(columnSpecification = columnSpecification)

        return request<ColumnSpecification, DistinctElementsResult>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation findDistinctElementsByColumn
     *
     * @param columnSpecification  (optional)
     * @return RequestConfig
     */
    fun findDistinctElementsByColumnRequestConfig(columnSpecification: ColumnSpecification?) : RequestConfig<ColumnSpecification> {
        val localVariableBody = columnSpecification
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Content-Type"] = "application/json"
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/api/v1/find/boolean/column/distinct",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

    /**
     * Find all elements of given columns
     * Find all elements of given columns
     * @param selectSpecification  (optional)
     * @return SelectResult
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun selectFromTable(selectSpecification: SelectSpecification? = null) : SelectResult {
        val localVarResponse = selectFromTableWithHttpInfo(selectSpecification = selectSpecification)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as SelectResult
            ResponseType.Informational -> throw UnsupportedOperationException("Client does not support Informational responses.")
            ResponseType.Redirection -> throw UnsupportedOperationException("Client does not support Redirection responses.")
            ResponseType.ClientError -> {
                val localVarError = localVarResponse as ClientError<*>
                throw ClientException("Client error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
            ResponseType.ServerError -> {
                val localVarError = localVarResponse as ServerError<*>
                throw ServerException("Server error : ${localVarError.statusCode} ${localVarError.message.orEmpty()}", localVarError.statusCode, localVarResponse)
            }
        }
    }

    /**
     * Find all elements of given columns
     * Find all elements of given columns
     * @param selectSpecification  (optional)
     * @return ApiResponse<SelectResult?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun selectFromTableWithHttpInfo(selectSpecification: SelectSpecification?) : ApiResponse<SelectResult?> {
        val localVariableConfig = selectFromTableRequestConfig(selectSpecification = selectSpecification)

        return request<SelectSpecification, SelectResult>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation selectFromTable
     *
     * @param selectSpecification  (optional)
     * @return RequestConfig
     */
    fun selectFromTableRequestConfig(selectSpecification: SelectSpecification?) : RequestConfig<SelectSpecification> {
        val localVariableBody = selectSpecification
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Content-Type"] = "application/json"
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/api/v1/find/boolean/table/select",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

}
