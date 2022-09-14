# ch.unibas.dmi.dbis.cineast.client - Kotlin client library for Cineast RESTful API

## Requires

* Kotlin 1.4.30
* Gradle 6.8.3

## Build

First, create the gradle wrapper script:

```
gradle wrapper
```

Then, run:

```
./gradlew check assemble
```

This runs all tests and packages the library.

## Features/Implementation Notes

* Supports JSON inputs/outputs, File inputs, and Form inputs.
* Supports collection formats for query parameters: csv, tsv, ssv, pipes.
* Some Kotlin and Java types are fully qualified to avoid conflicts with types defined in OpenAPI definitions.
* Implementation of ApiClient is intended to reduce method counts, specifically to benefit Android targets.

<a name="documentation-for-api-endpoints"></a>
## Documentation for API Endpoints

All URIs are relative to *http://localhost*

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*DefaultApi* | [**getObjectsWithId**](docs/DefaultApi.md#getobjectswithid) | **GET** /objects/{id} | Get objects with id
*DefaultApi* | [**getThumbnailsWithId**](docs/DefaultApi.md#getthumbnailswithid) | **GET** /thumbnails/{id} | Get thumbnails with id
*MetadataApi* | [**findFeaturesByCategory**](docs/MetadataApi.md#findfeaturesbycategory) | **POST** /api/v1/find/feature/all/by/category/{category} | Find features for the given category for all (or specific) IDs
*MetadataApi* | [**findFeaturesByEntity**](docs/MetadataApi.md#findfeaturesbyentity) | **POST** /api/v1/find/feature/all/by/entity/{entity} | Find features for the given entity name for all (or specific) IDs
*MetadataApi* | [**findMetaById**](docs/MetadataApi.md#findmetabyid) | **GET** /api/v1/find/metadata/by/id/{id} | Find metadata for the given object id
*MetadataApi* | [**findMetaFullyQualified**](docs/MetadataApi.md#findmetafullyqualified) | **GET** /api/v1/find/metadata/of/{id}/in/{domain}/with/{key} | Find metadata for specific object id in given domain with given key
*MetadataApi* | [**findMetadataByDomain**](docs/MetadataApi.md#findmetadatabydomain) | **GET** /api/v1/find/metadata/in/{domain}/by/id/{id} | Find metadata for specific object id in given domain
*MetadataApi* | [**findMetadataByDomainBatched**](docs/MetadataApi.md#findmetadatabydomainbatched) | **POST** /api/v1/find/metadata/in/{domain} | Find metadata in the specified domain for all the given ids
*MetadataApi* | [**findMetadataByKey**](docs/MetadataApi.md#findmetadatabykey) | **GET** /api/v1/find/metadata/with/{key}/by/id/{id} | Find metadata for a given object id with specified key
*MetadataApi* | [**findMetadataByKeyBatched**](docs/MetadataApi.md#findmetadatabykeybatched) | **POST** /api/v1/find/metadata/with/{key} | Find metadata for a given object id with specified key
*MetadataApi* | [**findMetadataForObjectIdBatched**](docs/MetadataApi.md#findmetadataforobjectidbatched) | **POST** /api/v1/find/metadata/by/id | Finds metadata for the given list of object ids
*MetadataApi* | [**findSegFeatById**](docs/MetadataApi.md#findsegfeatbyid) | **GET** /api/v1/find/feature/all/by/id/{id} | Find features for the given id
*MetadataApi* | [**findSegMetaById**](docs/MetadataApi.md#findsegmetabyid) | **GET** /api/v1/find/metadata/by/segmentid/{id} | Find metadata for the given segment id
*MetadataApi* | [**findTagInformationById**](docs/MetadataApi.md#findtaginformationbyid) | **GET** /api/v1/find/feature/tags/by/id/{id} | Find tag ids for the given id
*MetadataApi* | [**findTextByIDAndCat**](docs/MetadataApi.md#findtextbyidandcat) | **GET** /api/v1/find/feature/text/by/{id}/{category} | Find Text for the given id and retrieval category
*MiscApi* | [**countRows**](docs/MiscApi.md#countrows) | **GET** /api/v1/count/table/{table} | Count objects
*MiscApi* | [**findDistinctElementsByColumn**](docs/MiscApi.md#finddistinctelementsbycolumn) | **POST** /api/v1/find/boolean/column/distinct | Find all distinct elements of a given column
*MiscApi* | [**selectFromTable**](docs/MiscApi.md#selectfromtable) | **POST** /api/v1/find/boolean/table/select | Find all elements of given columns
*ObjectApi* | [**findObjectsAll**](docs/ObjectApi.md#findobjectsall) | **GET** /api/v1/find/objects/all | Find all objects for a certain type
*ObjectApi* | [**findObjectsByAttribute**](docs/ObjectApi.md#findobjectsbyattribute) | **GET** /api/v1/find/object/by/{attribute}/{value} | Find object by specified attribute value. I.e by id, name or path
*ObjectApi* | [**findObjectsByIdBatched**](docs/ObjectApi.md#findobjectsbyidbatched) | **POST** /api/v1/find/object/by/id | Find objects by id
*ObjectApi* | [**findObjectsPagination**](docs/ObjectApi.md#findobjectspagination) | **GET** /api/v1/find/object/all/{skip}/{limit} | Get a fixed amount of objects from the sorted list
*SegmentApi* | [**findSegmentById**](docs/SegmentApi.md#findsegmentbyid) | **GET** /api/v1/find/segments/by/id/{id} | Finds segments for specified id
*SegmentApi* | [**findSegmentByIdBatched**](docs/SegmentApi.md#findsegmentbyidbatched) | **POST** /api/v1/find/segments/by/id | Finds segments for specified ids
*SegmentApi* | [**findSegmentByObjectId**](docs/SegmentApi.md#findsegmentbyobjectid) | **GET** /api/v1/find/segments/all/object/{id} | Find segments by their media object's id
*SegmentsApi* | [**findSegmentSimilar**](docs/SegmentsApi.md#findsegmentsimilar) | **POST** /api/v1/find/segments/similar | Find similar segments based on the given query
*SegmentsApi* | [**findSegmentSimilarStaged**](docs/SegmentsApi.md#findsegmentsimilarstaged) | **POST** /api/v1/find/segments/similar/staged | Find similar segments based on the given staged query
*SegmentsApi* | [**findSegmentSimilarTemporal**](docs/SegmentsApi.md#findsegmentsimilartemporal) | **POST** /api/v1/find/segments/similar/temporal | Find similar segments based on the given temporal query
*SessionApi* | [**extractItem**](docs/SessionApi.md#extractitem) | **POST** /api/v1/extract/new | Extract new item
*SessionApi* | [**startExtraction**](docs/SessionApi.md#startextraction) | **POST** /api/v1/extract/start | Start a new extraction session
*SessionApi* | [**stopExtraction**](docs/SessionApi.md#stopextraction) | **POST** /api/v1/extract/stop | Stop the active extraction session
*StatusApi* | [**status**](docs/StatusApi.md#status) | **GET** /api/v1/status | Get the status of the server
*TagApi* | [**findAllTags**](docs/TagApi.md#findalltags) | **GET** /api/v1/find/tags/all | Find all tags
*TagApi* | [**findTagsBy**](docs/TagApi.md#findtagsby) | **GET** /api/v1/find/tags/by/{attribute}/{value} | Find all tags specified by attribute value
*TagApi* | [**findTagsById**](docs/TagApi.md#findtagsbyid) | **POST** /api/v1/tags/by/id | Find all tags by ids


<a name="documentation-for-models"></a>
## Documentation for Models

 - [ch.unibas.dmi.dbis.cineast.client.models.AbstractMetadataFilterDescriptor](docs/AbstractMetadataFilterDescriptor.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.ColumnSpecification](docs/ColumnSpecification.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.DistinctElementsResult](docs/DistinctElementsResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.ExtractionContainerMessage](docs/ExtractionContainerMessage.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.ExtractionItemContainer](docs/ExtractionItemContainer.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.FeaturesAllCategoriesQueryResult](docs/FeaturesAllCategoriesQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.FeaturesByCategoryQueryResult](docs/FeaturesByCategoryQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.FeaturesByEntityQueryResult](docs/FeaturesByEntityQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.FeaturesTextCategoryQueryResult](docs/FeaturesTextCategoryQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.IdList](docs/IdList.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.IntegerMessage](docs/IntegerMessage.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaObjectDescriptor](docs/MediaObjectDescriptor.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaObjectMetadataDescriptor](docs/MediaObjectMetadataDescriptor.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaObjectMetadataQueryResult](docs/MediaObjectMetadataQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaObjectQueryResult](docs/MediaObjectQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaSegmentDescriptor](docs/MediaSegmentDescriptor.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaSegmentMetadataDescriptor](docs/MediaSegmentMetadataDescriptor.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaSegmentMetadataQueryResult](docs/MediaSegmentMetadataQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MediaSegmentQueryResult](docs/MediaSegmentQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MetadataAccessSpecification](docs/MetadataAccessSpecification.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MetadataDomainFilter](docs/MetadataDomainFilter.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.MetadataKeyFilter](docs/MetadataKeyFilter.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.OptionallyFilteredIdList](docs/OptionallyFilteredIdList.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.Ping](docs/Ping.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.QueryConfig](docs/QueryConfig.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.QueryStage](docs/QueryStage.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.QueryTerm](docs/QueryTerm.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.SelectResult](docs/SelectResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.SelectSpecification](docs/SelectSpecification.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.SessionMessage](docs/SessionMessage.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.SimilarityQuery](docs/SimilarityQuery.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.SimilarityQueryResult](docs/SimilarityQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.SimilarityQueryResultBatch](docs/SimilarityQueryResultBatch.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.StagedSimilarityQuery](docs/StagedSimilarityQuery.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.StringDoublePair](docs/StringDoublePair.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.Tag](docs/Tag.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.TagIDsForElementQueryResult](docs/TagIDsForElementQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.TagsQueryResult](docs/TagsQueryResult.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.TemporalObject](docs/TemporalObject.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.TemporalQuery](docs/TemporalQuery.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.TemporalQueryConfig](docs/TemporalQueryConfig.md)
 - [ch.unibas.dmi.dbis.cineast.client.models.TemporalQueryResult](docs/TemporalQueryResult.md)


<a name="documentation-for-authorization"></a>
## Documentation for Authorization

All endpoints do not require authorization.
