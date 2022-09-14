package ch.unibas.dmi.dbis.collector.core.model.query

import org.threeten.extra.Interval
import java.time.Instant

abstract class SubQuery(
    queryData: QueryData
) : Query(queryData) {

    abstract var superId: Long?
    abstract val temporalType: TemporalType
    abstract val apiKey: String

    abstract fun createRecoveryCopy(
        label: String = this.label,
        temporalType: TemporalType = this.temporalType,
        interval: Interval = this.interval,
        id: Long? = null // Should be null since we're creating a copy, i.e., a new query.
    ): SubQuery

}

data class SubQueryInterval(
    val interval: Interval,
    val temporalType: TemporalType
) {

    companion object {

        /*
         * Setting the second parameter to the end of the interval could be used to enforce a
         * search query.
         */
        fun deriveFromInterval(
            interval: Interval,
            splitInstant: Instant = Instant.now()
        ): List<SubQueryInterval> {
            val sqis = mutableListOf<SubQueryInterval>()

            if (interval.isBefore(splitInstant)) {
                // Interval has already passed.
                sqis.add(SubQueryInterval(interval, TemporalType.SEARCH))
            } else if (interval.contains(splitInstant)) {
                // Interval is currently ongoing.
                sqis.add(
                    SubQueryInterval(
                        Interval.of(interval.start, splitInstant),
                        TemporalType.SEARCH
                    )
                )
                sqis.add(
                    SubQueryInterval(
                        Interval.of(splitInstant, interval.end),
                        TemporalType.STREAM
                    )
                )
            } else {
                // Interval starts in the future.
                sqis.add(SubQueryInterval(interval, TemporalType.STREAM))
            }

            return sqis
        }

    }

}
