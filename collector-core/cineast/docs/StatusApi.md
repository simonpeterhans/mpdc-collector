# StatusApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**status**](StatusApi.md#status) | **GET** /api/v1/status | Get the status of the server


<a name="status"></a>
# **status**
> Ping status()

Get the status of the server

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = StatusApi()
try {
    val result : Ping = apiInstance.status()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling StatusApi#status")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling StatusApi#status")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**Ping**](Ping.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

