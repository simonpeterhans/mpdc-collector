package ch.unibas.dmi.dbis.collector.core.util

data class Version(val major: Int, val minor: Int, val patch: Int) : Comparable<Version> {

    companion object {
        fun fromString(s: String): Version {
            val split = s.split(".")
            return Version(
                Integer.parseInt(split[0]),
                Integer.parseInt(split[1]),
                Integer.parseInt(split[2])
            )
        }
    }

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    override fun compareTo(other: Version): Int {
        if (other == this) {
            return 0
        }

        var diff = major - other.major

        if (diff == 0) {
            diff = minor - other.minor

            if (diff == 0) {
                diff = patch - other.patch
            }
        }

        return diff
    }

}
