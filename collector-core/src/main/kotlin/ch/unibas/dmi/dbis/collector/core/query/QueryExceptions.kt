package ch.unibas.dmi.dbis.collector.core.query

class QueryNotPersistedException : Exception("Query has not been persisted yet!")

class NewQueryHasIdException : Exception("New queries cannot have an ID!")

class UnknownSuperQueryIdException : Exception("No super query with the provided ID exists!")

class UnknownSubQueryIdException : Exception("No sub query with the provided ID exists!")

class NoRunningSubQueryWithIdException :
    Exception("No sub query with the provided ID is currently running!")

class SubQueryAlreadyRunningException : Exception("This query is already running!")

class SuperQueryWithoutSubQueryException :
    Exception("A super query must have at least one sub query!")

class SuperQueryLabelExistsException : Exception("A super query's label must be unique!")
