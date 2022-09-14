# DefaultApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**getObjectsWithId**](DefaultApi.md#getObjectsWithId) | **GET** /objects/{id} | Get objects with id
[**getThumbnailsWithId**](DefaultApi.md#getThumbnailsWithId) | **GET** /thumbnails/{id} | Get thumbnails with id


<a name="getObjectsWithId"></a>
# **getObjectsWithId**
> getObjectsWithId(id)

Get objects with id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = DefaultApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.getObjectsWithId(id)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#getObjectsWithId")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#getObjectsWithId")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

<a name="getThumbnailsWithId"></a>
# **getThumbnailsWithId**
> getThumbnailsWithId(id)

Get thumbnails with id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = DefaultApi()
val id : kotlin.String = id_example // kotlin.String | 
try {
    apiInstance.getThumbnailsWithId(id)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#getThumbnailsWithId")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#getThumbnailsWithId")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**|  |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined

