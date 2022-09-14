package ch.unibas.dmi.dbis.collector.core.dal.storage

interface Dao<PK : Comparable<PK>, D : Dto<PK>> {

    fun getAll(): List<D>

    fun findByKey(key: PK): D?

    fun getByKey(key: PK): D

    fun existsByKey(key: PK): Boolean

    fun insertDto(dto: D): PK

    fun updateDto(dto: D): Boolean

    fun upsertDto(dto: D): PK?

    fun deleteByKey(key: PK): Boolean

    fun deleteByKeyIfExists(key: PK): Boolean

}
