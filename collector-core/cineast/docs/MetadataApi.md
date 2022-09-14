# MetadataApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findFeaturesByCategory**](MetadataApi.md#findFeaturesByCategory) | **POST** /api/v1/find/feature/all/by/category/{category} | Find features for the given category for all (or specific) IDs
[**findFeaturesByEntity**](MetadataApi.md#findFeaturesByEntity) | **POST** /api/v1/find/feature/all/by/entity/{entity} | Find features for the given entity name for all (or specific) IDs
[**findMetaById**](MetadataApi.md#findMetaById) | **GET** /api/v1/find/metadata/by/id/{id} | Find metadata for the given object id
[**findMetaFullyQualified**](MetadataApi.md#findMetaFullyQualified) | **GET** /api/v1/find/metadata/of/{id}/in/{domain}/with/{key} | Find metadata for specific object id in given domain with given key
[**findMetadataByDomain**](MetadataApi.md#findMetadataByDomain) | **GET** /api/v1/find/metadata/in/{domain}/by/id/{id} | Find metadata for specific object id in given domain
[**findMetadataByDomainBatched**](MetadataApi.md#findMetadataByDomainBatched) | **POST** /api/v1/find/metadata/in/{domain} | Find metadata in the specified domain for all the given ids
[**findMetadataByKey**](MetadataApi.md#findMetadataByKey) | **GET** /api/v1/find/metadata/with/{key}/by/id/{id} | Find metadata for a given object id with specified key
[**findMetadataByKeyBatched**](MetadataApi.md#findMetadataByKeyBatched) | **POST** /api/v1/find/metadata/with/{key} | Find metadata for a given object id with specified key
[**findMetadataForObjectIdBatched**](MetadataApi.md#findMetadataForObjectIdBatched) | **POST** /api/v1/find/metadata/by/id | Finds metadata for the given list of object ids
[**findSegFeatById**](MetadataApi.md#findSegFeatById) | **GET** /api/v1/find/feature/all/by/id/{id} | Find features for the given id
[**findSegMetaById**](MetadataApi.md#findSegMetaById) | **GET** /api/v1/find/metadata/by/segmentid/{id} | Find metadata for the given segment id
[**findTagInformationById**](MetadataApi.md#findTagInformationById) | **GET** /api/v1/find/feature/tags/by/id/{id} | Find tag ids for the given id
[**findTextByIDAndCat**](MetadataApi.md#findTextByIDAndCat) | **GET** /api/v1/find/feature/text/by/{id}/{category} | Find Text for the given id and retrieval category


<a name="findFeaturesByCategory"></a>
# **findFeaturesByCategory**
> FeaturesByCategoryQueryResult findFeaturesByCategory(category, idList)

Find features for the given category for all (or specific) IDs

Find features for the given category for all (or specific) IDs

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val category : kotlin.String = category_example // kotlin.String | 
val idList : IdList =  // IdList | 
try {
    val result : FeaturesByCategoryQueryResult = apiInstance.findFeaturesByCategory(category, idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findFeaturesByCategory")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findFeaturesByCategory")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **category** | **kotlin.String**|  |
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**FeaturesByCategoryQueryResult**](FeaturesByCategoryQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findFeaturesByEntity"></a>
# **findFeaturesByEntity**
> FeaturesByEntityQueryResult findFeaturesByEntity(entity, idList)

Find features for the given entity name for all (or specific) IDs

Find features for the given entity name for all (or specific) IDs

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val entity : kotlin.String = entity_example // kotlin.String | 
val idList : IdList =  // IdList | 
try {
    val result : FeaturesByEntityQueryResult = apiInstance.findFeaturesByEntity(entity, idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findFeaturesByEntity")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findFeaturesByEntity")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **entity** | **kotlin.String**|  |
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**FeaturesByEntityQueryResult**](FeaturesByEntityQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findMetaById"></a>
# **findMetaById**
> MediaObjectMetadataQueryResult findMetaById(id)

Find metadata for the given object id

Find metadata by the given object id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val id : kotlin.String = id_example // kotlin.String | The object id to find metadata of
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetaById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetaById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetaById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The object id to find metadata of |

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findMetaFullyQualified"></a>
# **findMetaFullyQualified**
> MediaObjectMetadataQueryResult findMetaFullyQualified(id, domain, key)

Find metadata for specific object id in given domain with given key

The description

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val id : kotlin.String = id_example // kotlin.String | The object id
val domain : kotlin.String = domain_example // kotlin.String | The domain name
val key : kotlin.String = key_example // kotlin.String | Metadata key
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetaFullyQualified(id, domain, key)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetaFullyQualified")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetaFullyQualified")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The object id |
 **domain** | **kotlin.String**| The domain name |
 **key** | **kotlin.String**| Metadata key |

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findMetadataByDomain"></a>
# **findMetadataByDomain**
> MediaObjectMetadataQueryResult findMetadataByDomain(domain, id)

Find metadata for specific object id in given domain

Find metadata for specific object id in given domain

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val domain : kotlin.String = domain_example // kotlin.String | The domain of the metadata to find
val id : kotlin.String = id_example // kotlin.String | The object id of the multimedia object to find metadata for
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetadataByDomain(domain, id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetadataByDomain")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetadataByDomain")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **kotlin.String**| The domain of the metadata to find |
 **id** | **kotlin.String**| The object id of the multimedia object to find metadata for |

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findMetadataByDomainBatched"></a>
# **findMetadataByDomainBatched**
> MediaObjectMetadataQueryResult findMetadataByDomainBatched(domain, idList)

Find metadata in the specified domain for all the given ids

Find metadata in the specified domain for all the given ids

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val domain : kotlin.String = domain_example // kotlin.String | The domain of the metadata to find
val idList : IdList =  // IdList | 
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetadataByDomainBatched(domain, idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetadataByDomainBatched")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetadataByDomainBatched")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **kotlin.String**| The domain of the metadata to find |
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findMetadataByKey"></a>
# **findMetadataByKey**
> MediaObjectMetadataQueryResult findMetadataByKey(key, id)

Find metadata for a given object id with specified key

Find metadata for a given object id with specified key

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val key : kotlin.String = key_example // kotlin.String | The key of the metadata to find
val id : kotlin.String = id_example // kotlin.String | The object id of for which the metadata should be find
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetadataByKey(key, id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetadataByKey")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetadataByKey")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **key** | **kotlin.String**| The key of the metadata to find |
 **id** | **kotlin.String**| The object id of for which the metadata should be find |

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findMetadataByKeyBatched"></a>
# **findMetadataByKeyBatched**
> MediaObjectMetadataQueryResult findMetadataByKeyBatched(key, idList)

Find metadata for a given object id with specified key

Find metadata with a the speicifed key from the path across all domains and for the provided ids

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val key : kotlin.String = key_example // kotlin.String | The key of the metadata to find
val idList : IdList =  // IdList | 
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetadataByKeyBatched(key, idList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetadataByKeyBatched")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetadataByKeyBatched")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **key** | **kotlin.String**| The key of the metadata to find |
 **idList** | [**IdList**](IdList.md)|  | [optional]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findMetadataForObjectIdBatched"></a>
# **findMetadataForObjectIdBatched**
> MediaObjectMetadataQueryResult findMetadataForObjectIdBatched(optionallyFilteredIdList)

Finds metadata for the given list of object ids

Finds metadata for the given list of object ids

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val optionallyFilteredIdList : OptionallyFilteredIdList =  // OptionallyFilteredIdList | 
try {
    val result : MediaObjectMetadataQueryResult = apiInstance.findMetadataForObjectIdBatched(optionallyFilteredIdList)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findMetadataForObjectIdBatched")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findMetadataForObjectIdBatched")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **optionallyFilteredIdList** | [**OptionallyFilteredIdList**](OptionallyFilteredIdList.md)|  | [optional]

### Return type

[**MediaObjectMetadataQueryResult**](MediaObjectMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findSegFeatById"></a>
# **findSegFeatById**
> FeaturesAllCategoriesQueryResult findSegFeatById(id)

Find features for the given id

Find features by the given id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val id : kotlin.String = id_example // kotlin.String | The id to find features of
try {
    val result : FeaturesAllCategoriesQueryResult = apiInstance.findSegFeatById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findSegFeatById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findSegFeatById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The id to find features of |

### Return type

[**FeaturesAllCategoriesQueryResult**](FeaturesAllCategoriesQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findSegMetaById"></a>
# **findSegMetaById**
> MediaSegmentMetadataQueryResult findSegMetaById(id)

Find metadata for the given segment id

Find metadata by the given segment id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val id : kotlin.String = id_example // kotlin.String | The segment id to find metadata of
try {
    val result : MediaSegmentMetadataQueryResult = apiInstance.findSegMetaById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findSegMetaById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findSegMetaById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The segment id to find metadata of |

### Return type

[**MediaSegmentMetadataQueryResult**](MediaSegmentMetadataQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findTagInformationById"></a>
# **findTagInformationById**
> TagIDsForElementQueryResult findTagInformationById(id)

Find tag ids for the given id

Find tag ids for the given id

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val id : kotlin.String = id_example // kotlin.String | The id to find tagids of
try {
    val result : TagIDsForElementQueryResult = apiInstance.findTagInformationById(id)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findTagInformationById")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findTagInformationById")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The id to find tagids of |

### Return type

[**TagIDsForElementQueryResult**](TagIDsForElementQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findTextByIDAndCat"></a>
# **findTextByIDAndCat**
> FeaturesTextCategoryQueryResult findTextByIDAndCat(id, category)

Find Text for the given id and retrieval category

Find Text by the given id and retrieval category

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MetadataApi()
val id : kotlin.String = id_example // kotlin.String | The id to find text of
val category : kotlin.String = category_example // kotlin.String | The category for which retrieval shall be performed
try {
    val result : FeaturesTextCategoryQueryResult = apiInstance.findTextByIDAndCat(id, category)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MetadataApi#findTextByIDAndCat")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MetadataApi#findTextByIDAndCat")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **id** | **kotlin.String**| The id to find text of |
 **category** | **kotlin.String**| The category for which retrieval shall be performed |

### Return type

[**FeaturesTextCategoryQueryResult**](FeaturesTextCategoryQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

