# MiscApi

All URIs are relative to *http://localhost*

Method | HTTP request | Description
------------- | ------------- | -------------
[**countRows**](MiscApi.md#countRows) | **GET** /api/v1/count/table/{table} | Count objects
[**findDistinctElementsByColumn**](MiscApi.md#findDistinctElementsByColumn) | **POST** /api/v1/find/boolean/column/distinct | Find all distinct elements of a given column
[**selectFromTable**](MiscApi.md#selectFromTable) | **POST** /api/v1/find/boolean/table/select | Find all elements of given columns


<a name="countRows"></a>
# **countRows**
> IntegerMessage countRows(table)

Count objects

Equivalent to calling SELECT count(*) FROM table. Used to determined #pages for pagination in a frontend or statistical purposes

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MiscApi()
val table : kotlin.String = table_example // kotlin.String | 
try {
    val result : IntegerMessage = apiInstance.countRows(table)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MiscApi#countRows")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MiscApi#countRows")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **table** | **kotlin.String**|  |

### Return type

[**IntegerMessage**](IntegerMessage.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a name="findDistinctElementsByColumn"></a>
# **findDistinctElementsByColumn**
> DistinctElementsResult findDistinctElementsByColumn(columnSpecification)

Find all distinct elements of a given column

Find all distinct elements of a given column. Please note that this operation does cache results.

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MiscApi()
val columnSpecification : ColumnSpecification =  // ColumnSpecification | 
try {
    val result : DistinctElementsResult = apiInstance.findDistinctElementsByColumn(columnSpecification)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MiscApi#findDistinctElementsByColumn")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MiscApi#findDistinctElementsByColumn")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **columnSpecification** | [**ColumnSpecification**](ColumnSpecification.md)|  | [optional]

### Return type

[**DistinctElementsResult**](DistinctElementsResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="selectFromTable"></a>
# **selectFromTable**
> SelectResult selectFromTable(selectSpecification)

Find all elements of given columns

Find all elements of given columns

### Example
```kotlin
// Import classes:
//import ch.unibas.dmi.dbis.cineast.client.infrastructure.*
//import ch.unibas.dmi.dbis.cineast.client.models.*

val apiInstance = MiscApi()
val selectSpecification : SelectSpecification =  // SelectSpecification | 
try {
    val result : SelectResult = apiInstance.selectFromTable(selectSpecification)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling MiscApi#selectFromTable")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling MiscApi#selectFromTable")
    e.printStackTrace()
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **selectSpecification** | [**SelectSpecification**](SelectSpecification.md)|  | [optional]

### Return type

[**SelectResult**](SelectResult.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

