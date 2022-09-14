# SegmentsApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**findSegmentSimilar**](SegmentsApi.md#findSegmentSimilar) | **POST** /api/v1/find/segments/similar | Find similar segments based on the given query
[**findSegmentSimilarStaged**](SegmentsApi.md#findSegmentSimilarStaged) | **POST** /api/v1/find/segments/similar/staged | Find similar segments based on the given staged query
[**findSegmentSimilarTemporal**](SegmentsApi.md#findSegmentSimilarTemporal) | **POST** /api/v1/find/segments/similar/temporal | Find similar segments based on the given temporal query


<a name="findSegmentSimilar"></a>
# **findSegmentSimilar**
> SimilarityQueryResultBatch findSegmentSimilar(similarityQuery)

Find similar segments based on the given query

Performs a similarity search based on the formulated query

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SegmentsApi()
val similarityQuery : SimilarityQuery =  // SimilarityQuery | 
try {
    val result : SimilarityQueryResultBatch = apiInstance.findSegmentSimilar(similarityQuery)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SegmentsApi#findSegmentSimilar")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SegmentsApi#findSegmentSimilar")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **similarityQuery** | [**SimilarityQuery**](SimilarityQuery.md)|  | [optional]

### Return type

[**SimilarityQueryResultBatch**](SimilarityQueryResultBatch.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findSegmentSimilarStaged"></a>
# **findSegmentSimilarStaged**
> SimilarityQueryResultBatch findSegmentSimilarStaged(stagedSimilarityQuery)

Find similar segments based on the given staged query

Performs a similarity search based on the formulated query stages, executing each subsequent stage on the results of the previous stage

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SegmentsApi()
val stagedSimilarityQuery : StagedSimilarityQuery =  // StagedSimilarityQuery | 
try {
    val result : SimilarityQueryResultBatch = apiInstance.findSegmentSimilarStaged(stagedSimilarityQuery)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SegmentsApi#findSegmentSimilarStaged")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SegmentsApi#findSegmentSimilarStaged")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **stagedSimilarityQuery** | [**StagedSimilarityQuery**](StagedSimilarityQuery.md)|  | [optional]

### Return type

[**SimilarityQueryResultBatch**](SimilarityQueryResultBatch.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="findSegmentSimilarTemporal"></a>
# **findSegmentSimilarTemporal**
> TemporalQueryResult findSegmentSimilarTemporal(temporalQuery)

Find similar segments based on the given temporal query

Performs a similarity search based on the formulated query stages in the given temporal order, scoring final results by their similarity to the specified temporal context

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = SegmentsApi()
val temporalQuery : TemporalQuery =  // TemporalQuery | 
try {
    val result : TemporalQueryResult = apiInstance.findSegmentSimilarTemporal(temporalQuery)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling SegmentsApi#findSegmentSimilarTemporal")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling SegmentsApi#findSegmentSimilarTemporal")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **temporalQuery** | [**TemporalQuery**](TemporalQuery.md)|  | [optional]

### Return type

[**TemporalQueryResult**](TemporalQueryResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

