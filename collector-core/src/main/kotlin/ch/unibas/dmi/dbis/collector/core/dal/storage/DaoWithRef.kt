package ch.unibas.dmi.dbis.collector.core.dal.storage

interface DaoWithRef<PK : Comparable<PK>, FK : Comparable<FK>, D : DtoWithRef<PK, FK>> :
    Dao<PK, D> {

    fun findByRef(ref: FK): D?

    fun getByRef(ref: FK): List<D>

    fun existsByRef(ref: FK): Boolean

    // insertDtoByRef() makes little sense here, use insertDto() instead.

    fun updateDtoByRef(dto: D): Boolean

    fun upsertDtoByRef(dto: D): PK?

    fun deleteByRef(ref: FK): Boolean

    fun deleteByRefIfExists(ref: FK): Boolean

}
