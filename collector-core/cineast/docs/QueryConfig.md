
# QueryConfig

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**queryId** | **kotlin.String** |  |  [optional]
**hints** | [**inline**](#kotlin.collections.Set&lt;Hints&gt;) |  |  [optional]
**relevantSegmentIds** | **kotlin.collections.Set&lt;kotlin.String&gt;** |  |  [optional]
**distance** | [**inline**](#Distance) |  |  [optional]
**distanceWeights** | **kotlin.collections.List&lt;kotlin.Float&gt;** |  |  [optional]
**norm** | **kotlin.Float** |  |  [optional]
**resultsPerModule** | **kotlin.Int** |  |  [optional]
**maxResults** | **kotlin.Int** |  |  [optional]
**distanceIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**correspondenceFunctionIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**correspondenceFunction** | [**kotlin.Any**](.md) |  |  [optional]
**distanceWeightsIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**normIfEmpty** | [**QueryConfig**](QueryConfig.md) |  |  [optional]
**rawResultsPerModule** | **kotlin.Int** |  |  [optional]


<a name="kotlin.collections.Set<Hints>"></a>
## Enum: hints
Name | Value
---- | -----
hints | exact, inexact, lsh, ecp, mi, pq, sh, va, vaf, vav, sequential, empirical


<a name="Distance"></a>
## Enum: distance
Name | Value
---- | -----
distance | chisquared, correlation, cosine, hamming, jaccard, kullbackleibler, chebyshev, euclidean, squaredeuclidean, manhattan, minkowski, spannorm, haversine



