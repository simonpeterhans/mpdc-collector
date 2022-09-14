# TagApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findAllTags**](TagApi.md#findAllTags) | **GET** /api/v1/find/tags/all | Find all tags
[**findTagsBy**](TagApi.md#findTagsBy) | **GET** /api/v1/find/tags/by/{attribute}/{value} | Find all tags specified by attribute value
[**findTagsById**](TagApi.md#findTagsById) | **POST** /api/v1/tags/by/id | Find all tags by ids


<a name="findAllTags"></a>
# **findAllTags**
> TagsQueryResult findAllTags()

Find all tags

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = TagApi()
try {
    val result : TagsQueryResult = apiInstance.findAllTags()
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TagApi#findAllTags")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TagApi#findAllTags")
    e.printStackTrace()
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**TagsQueryResult**](TagsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findTagsBy"></a>
# **findTagsBy**
> TagsQueryResult findTagsBy(attribute, `value`)

Find all tags specified by attribute value

Find all tags by attributes id, name or matchingname and filter value

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = TagApi()
val attribute : kotlin.String = attribute_example // kotlin.String | The attribute to filter on. One of: id, name, matchingname
val `value` : kotlin.String = `value`_example // kotlin.String | The value of the attribute to filter
try {
    val result : TagsQueryResult = apiInstance.findTagsBy(attribute, `value`)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TagApi#findTagsBy")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TagApi#findTagsBy")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **attribute** | **kotlin.String**| The attribute to filter on. One of: id, name, matchingname |
 **&#x60;value&#x60;** | **kotlin.String**| The value of the attribute to filter |

### Return type

[**TagsQueryResult**](TagsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findTagsById"></a>
# **findTagsById**
> TagsQueryResult findTagsById(idList)

Find all tags by ids

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = TagApi()
val idList : IdList =  // IdList | 
try {
    val result : TagsQueryResult = apiInstance.findTagsById(idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling TagApi#findTagsById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling TagApi#findTagsById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**TagsQueryResult**](TagsQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

