package ch.unibas.dmi.dbis.collector.core.pooling

import java.time.Instant
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalUnit
import kotlin.math.max

open class TimedIntervalPool(
    maxTickets: Int,
    private val refillUnit: TemporalUnit,
    refillsPerUnit: Int,
    private val refillDelayMs: Long,
    terminateOnDeactivate: Boolean,
) : IntervalPool(maxTickets, terminateOnDeactivate) {

    companion object {

        fun createAndStart(
            maxTickets: Int,
            refillUnit: TemporalUnit,
            refillsPerUnit: Int,
            refillDelayMs: Long,
            terminateOnDeactivate: Boolean
        ): TimedIntervalPool {
            val pool = TimedIntervalPool(
                maxTickets,
                refillUnit,
                refillsPerUnit,
                refillDelayMs,
                terminateOnDeactivate
            )
            pool.start()
            return pool
        }

    }

    private val intervalDurationMs = refillUnit.duration.toMillis() / refillsPerUnit

    override fun getRefillDelayMs(): Long {
        val now: Instant = Instant.now()
        val nowWithOffset: ZonedDateTime = now.atZone(ZoneOffset.UTC)
        val intervalStart: ZonedDateTime = nowWithOffset.truncatedTo(refillUnit)

        val currentPeriodMs = ChronoUnit.MILLIS.between(intervalStart, nowWithOffset)
        val completedIntervals = currentPeriodMs / intervalDurationMs

        val nextDate: ZonedDateTime = now
            .truncatedTo(refillUnit)
            .atZone(ZoneOffset.UTC)
            .plus(intervalDurationMs * (completedIntervals + 1), ChronoUnit.MILLIS)
            .plus(refillDelayMs, ChronoUnit.MILLIS) // Just to be sure (e.g., server-side delay).

        return max(0, ChronoUnit.MILLIS.between(now, nextDate)) // At least 0 (instant).
    }

}
