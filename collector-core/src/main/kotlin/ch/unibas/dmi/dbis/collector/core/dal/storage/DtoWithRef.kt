package ch.unibas.dmi.dbis.collector.core.dal.storage

abstract class DtoWithRef<PK : Comparable<PK>, FK : Comparable<FK>> : Dto<PK>() {

    abstract val ref: FK?

}
