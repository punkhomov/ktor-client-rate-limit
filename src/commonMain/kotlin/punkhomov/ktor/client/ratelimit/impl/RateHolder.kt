package punkhomov.ktor.client.ratelimit.impl

import punkhomov.ktor.client.ratelimit.core.Rate
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class RateHolder(rate: Rate) {
    val permitsPerPeriod = rate.permits

    val period = convertPeriodToMsUnit(rate)

    val permitted = LongContinuousBuffer(permitsPerPeriod, zeroInit)

    @OptIn(ExperimentalTime::class)
    private fun convertPeriodToMsUnit(rate: Rate) = Duration.convert(
        rate.periodTime.toDouble(), rate.periodUnit, DurationUnit.MILLISECONDS
    ).toLong()
}

fun createHoldersFrom(rates: List<Rate>) = rates.map(::RateHolder)

fun RateHolder.oldestPermit() = permitted.first()

fun RateHolder.newestPermit() = permitted.last()