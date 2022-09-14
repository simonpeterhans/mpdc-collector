# ObjectApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findObjectsAll**](ObjectApi.md#findObjectsAll) | **GET** /api/v1/find/objects/all | Find all objects for a certain type
[**findObjectsByAttribute**](ObjectApi.md#findObjectsByAttribute) | **GET** /api/v1/find/object/by/{attribute}/{value} | Find object by specified attribute value. I.e by id, name or path
[**findObjectsByIdBatched**](ObjectApi.md#findObjectsByIdBatched) | **POST** /api/v1/find/object/by/id | Find objects by id
[**findObjectsPagination**](ObjectApi.md#findObjectsPagination) | **GET** /api/v1/find/object/all/{skip}/{limit} | Get a fixed amount of objects from the sorted list


<a name="findObjectsAll"></a>
# **findObjectsAll**
> MediaObjectQueryResult findObjectsAll()

Find all objects for a certain type

Find all objects for a certain type

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = ObjectApi()
try {
    val result : MediaObjectQueryResult = apiInstance.findObjectsAll()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ObjectApi#findObjectsAll")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ObjectApi#findObjectsAll")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findObjectsByAttribute"></a>
# **findObjectsByAttribute**
> MediaObjectQueryResult findObjectsByAttribute(attribute, `value`)

Find object by specified attribute value. I.e by id, name or path

Find object by specified attribute value. I.e by id, name or path

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = ObjectApi()
val attribute : kotlin.String = attribute_example // kotlin.String | The attribute type of the value. One of: id, name, path
val `value` : kotlin.String = `value`_example // kotlin.String | The actual value that you want to filter for
try {
    val result : MediaObjectQueryResult = apiInstance.findObjectsByAttribute(attribute, `value`)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ObjectApi#findObjectsByAttribute")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ObjectApi#findObjectsByAttribute")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attribute** | **kotlin.String**| The attribute type of the value. One of: id, name, path |
 **&#x60;value&#x60;** | **kotlin.String**| The actual value that you want to filter for |

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findObjectsByIdBatched"></a>
# **findObjectsByIdBatched**
> MediaObjectQueryResult findObjectsByIdBatched(idList)

Find objects by id

Find objects by id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = ObjectApi()
val idList : IdList =  // IdList | 
try {
    val result : MediaObjectQueryResult = apiInstance.findObjectsByIdBatched(idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ObjectApi#findObjectsByIdBatched")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ObjectApi#findObjectsByIdBatched")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findObjectsPagination"></a>
# **findObjectsPagination**
> MediaObjectQueryResult findObjectsPagination(skip, limit)

Get a fixed amount of objects from the sorted list

Equivalent to calling SELECT * FROM multimediaobject ORDER BY objectid ASC LIMIT limit SKIP skip. Mostly used for pagination when wanting to retrieve all objects

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = ObjectApi()
val skip : kotlin.Int = 56 // kotlin.Int | How many objects should be skipped
val limit : kotlin.Int = 56 // kotlin.Int | How many object at most should be fetched
try {
    val result : MediaObjectQueryResult = apiInstance.findObjectsPagination(skip, limit)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling ObjectApi#findObjectsPagination")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling ObjectApi#findObjectsPagination")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **skip** | **kotlin.Int**| How many objects should be skipped |
 **limit** | **kotlin.Int**| How many object at most should be fetched |

### Return type

[**MediaObjectQueryResult**](MediaObjectQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

