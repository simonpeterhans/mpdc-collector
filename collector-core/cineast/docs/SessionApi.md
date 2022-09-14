# SessionApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**extractItem**](SessionApi.md#extractItem) | **POST** /api/v1/extract/new | Extract new item
[**startExtraction**](SessionApi.md#startExtraction) | **POST** /api/v1/extract/start | Start a new extraction session
[**stopExtraction**](SessionApi.md#stopExtraction) | **POST** /api/v1/extract/stop | Stop the active extraction session


<a name="extractItem"></a>
# **extractItem**
> SessionMessage extractItem(extractionContainerMessage)

Extract new item

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SessionApi()
val extractionContainerMessage : ExtractionContainerMessage =  // ExtractionContainerMessage | 
try {
    val result : SessionMessage = apiInstance.extractItem(extractionContainerMessage)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SessionApi#extractItem")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SessionApi#extractItem")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **extractionContainerMessage** | [**ExtractionContainerMessage**](ExtractionContainerMessage.md)|  | [optional]

### Return type

[**SessionMessage**](SessionMessage.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="startExtraction"></a>
# **startExtraction**
> SessionMessage startExtraction()

Start a new extraction session

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SessionApi()
try {
    val result : SessionMessage = apiInstance.startExtraction()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SessionApi#startExtraction")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SessionApi#startExtraction")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SessionMessage**](SessionMessage.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="stopExtraction"></a>
# **stopExtraction**
> SessionMessage stopExtraction()

Stop the active extraction session

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SessionApi()
try {
    val result : SessionMessage = apiInstance.stopExtraction()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SessionApi#stopExtraction")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SessionApi#stopExtraction")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**SessionMessage**](SessionMessage.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

