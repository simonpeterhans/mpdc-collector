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

import ch.unibas.dmi.dbis.cineast.client.models.ExtractionContainerMessage
import ch.unibas.dmi.dbis.cineast.client.models.SessionMessage

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

class SessionApi(basePath: kotlin.String = defaultBasePath, client: OkHttpClient = ApiClient.defaultClient) : ApiClient(basePath, client) {
    companion object {
        @JvmStatic
        val defaultBasePath: String by lazy {
            System.getProperties().getProperty(ApiClient.baseUrlKey, "http://localhost")
        }
    }

    /**
     * Extract new item
     * 
     * @param extractionContainerMessage  (optional)
     * @return SessionMessage
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun extractItem(extractionContainerMessage: ExtractionContainerMessage? = null) : SessionMessage {
        val localVarResponse = extractItemWithHttpInfo(extractionContainerMessage = extractionContainerMessage)

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as SessionMessage
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
     * Extract new item
     * 
     * @param extractionContainerMessage  (optional)
     * @return ApiResponse<SessionMessage?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun extractItemWithHttpInfo(extractionContainerMessage: ExtractionContainerMessage?) : ApiResponse<SessionMessage?> {
        val localVariableConfig = extractItemRequestConfig(extractionContainerMessage = extractionContainerMessage)

        return request<ExtractionContainerMessage, SessionMessage>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation extractItem
     *
     * @param extractionContainerMessage  (optional)
     * @return RequestConfig
     */
    fun extractItemRequestConfig(extractionContainerMessage: ExtractionContainerMessage?) : RequestConfig<ExtractionContainerMessage> {
        val localVariableBody = extractionContainerMessage
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Content-Type"] = "application/json"
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/api/v1/extract/new",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

    /**
     * Start a new extraction session
     * 
     * @return SessionMessage
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun startExtraction() : SessionMessage {
        val localVarResponse = startExtractionWithHttpInfo()

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as SessionMessage
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
     * Start a new extraction session
     * 
     * @return ApiResponse<SessionMessage?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun startExtractionWithHttpInfo() : ApiResponse<SessionMessage?> {
        val localVariableConfig = startExtractionRequestConfig()

        return request<Unit, SessionMessage>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation startExtraction
     *
     * @return RequestConfig
     */
    fun startExtractionRequestConfig() : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/api/v1/extract/start",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

    /**
     * Stop the active extraction session
     * 
     * @return SessionMessage
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     * @throws UnsupportedOperationException If the API returns an informational or redirection response
     * @throws ClientException If the API returns a client error response
     * @throws ServerException If the API returns a server error response
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class, UnsupportedOperationException::class, ClientException::class, ServerException::class)
    fun stopExtraction() : SessionMessage {
        val localVarResponse = stopExtractionWithHttpInfo()

        return when (localVarResponse.responseType) {
            ResponseType.Success -> (localVarResponse as Success<*>).data as SessionMessage
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
     * Stop the active extraction session
     * 
     * @return ApiResponse<SessionMessage?>
     * @throws IllegalStateException If the request is not correctly configured
     * @throws IOException Rethrows the OkHttp execute method exception
     */
    @Suppress("UNCHECKED_CAST")
    @Throws(IllegalStateException::class, IOException::class)
    fun stopExtractionWithHttpInfo() : ApiResponse<SessionMessage?> {
        val localVariableConfig = stopExtractionRequestConfig()

        return request<Unit, SessionMessage>(
            localVariableConfig
        )
    }

    /**
     * To obtain the request config of the operation stopExtraction
     *
     * @return RequestConfig
     */
    fun stopExtractionRequestConfig() : RequestConfig<Unit> {
        val localVariableBody = null
        val localVariableQuery: MultiValueMap = mutableMapOf()
        val localVariableHeaders: MutableMap<String, String> = mutableMapOf()
        localVariableHeaders["Accept"] = "application/json"

        return RequestConfig(
            method = RequestMethod.POST,
            path = "/api/v1/extract/stop",
            query = localVariableQuery,
            headers = localVariableHeaders,
            body = localVariableBody
        )
    }

}
