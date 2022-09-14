# SegmentApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findSegmentById**](SegmentApi.md#findSegmentById) | **GET** /api/v1/find/segments/by/id/{id} | Finds segments for specified id
[**findSegmentByIdBatched**](SegmentApi.md#findSegmentByIdBatched) | **POST** /api/v1/find/segments/by/id | Finds segments for specified ids
[**findSegmentByObjectId**](SegmentApi.md#findSegmentByObjectId) | **GET** /api/v1/find/segments/all/object/{id} | Find segments by their media object&#39;s id


<a name="findSegmentById"></a>
# **findSegmentById**
> MediaSegmentQueryResult findSegmentById(id)

Finds segments for specified id

Finds segments for specified id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SegmentApi()
val id : kotlin.String = id_example // kotlin.String | The id of the segments
try {
    val result : MediaSegmentQueryResult = apiInstance.findSegmentById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SegmentApi#findSegmentById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SegmentApi#findSegmentById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The id of the segments |

### Return type

[**MediaSegmentQueryResult**](MediaSegmentQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findSegmentByIdBatched"></a>
# **findSegmentByIdBatched**
> MediaSegmentQueryResult findSegmentByIdBatched(idList)

Finds segments for specified ids

Finds segments for specified ids

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SegmentApi()
val idList : IdList =  // IdList | 
try {
    val result : MediaSegmentQueryResult = apiInstance.findSegmentByIdBatched(idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SegmentApi#findSegmentByIdBatched")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SegmentApi#findSegmentByIdBatched")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaSegmentQueryResult**](MediaSegmentQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findSegmentByObjectId"></a>
# **findSegmentByObjectId**
> MediaSegmentQueryResult findSegmentByObjectId(id)

Find segments by their media object&#39;s id

Find segments by their media object&#39;s id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SegmentApi()
val id : kotlin.String = id_example // kotlin.String | The id of the media object to find segments fo
try {
    val result : MediaSegmentQueryResult = apiInstance.findSegmentByObjectId(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SegmentApi#findSegmentByObjectId")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SegmentApi#findSegmentByObjectId")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The id of the media object to find segments fo |

### Return type

[**MediaSegmentQueryResult**](MediaSegmentQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

