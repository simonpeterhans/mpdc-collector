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

import ch.unibas.dmi.dbis.cineast.client.models.Ping

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

class StatusApi(basePath: kotlin.String = defaultBasePath, client: OkHttpClient = ApiClient.defaultClient) : ApiClient(basePath, client) {
    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(ApiClient.baseUrlKey, "http://localhost")
        }
    }

    /**
     * Get the status of the server
     * 
     * @return Ping
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun status() : Ping {
        val localVarResponse = statusWithHttpInfo()

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as Ping
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
     * Get the status of the server
     * 
     * @return ApiResponse<Ping?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun statusWithHttpInfo() : ApiResponse<Ping?> {
        val localVariableConfig = statusRequestConfig()

        return request<Unit, Ping>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation status
     *
     * @return RequestConfig
     */
    fun statusRequestConfig() : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.GET,
            path = "/api/v1/status",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

}
