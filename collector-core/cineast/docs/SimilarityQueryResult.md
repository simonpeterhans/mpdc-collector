
# SimilarityQueryResult

## Properties
Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**queryId** | **kotlin.String** |  |  [optional]
**content** | [**kotlin.collections.List&lt;StringDoublePair&gt;**](StringDoublePair.md) |  |  [optional]
**category** | **kotlin.String** |  |  [optional]
**containerId** | **kotlin.Int** |  |  [optional]
**messageType** | [**inline**](#MessageType) |  |  [optional]


<a name="MessageType"></a>
## Enum: messageType
Name | Value
---- | -----
messageType | PING, Q_SIM, Q_MLT, Q_NESEG, Q_SEG, Q_TEMPORAL, SESSION_INFO, QR_START, QR_END, QR_ERROR, QR_OBJECT, QR_METADATA_O, QR_METADATA_S, QR_SEGMENT, QR_SIMILARITY, QR_TEMPORAL



