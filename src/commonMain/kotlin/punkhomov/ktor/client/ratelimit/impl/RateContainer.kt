package punkhomov.ktor.client.ratelimit.impl

import punkhomov.ktor.client.ratelimit.core.RateInfo
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

class RateContainer(val info: RateInfo) {
    val permits by info::permits

    @OptIn(ExperimentalTime::class)
    val period = Duration.convert(
        info.period.toDouble(), info.unit, DurationUnit.MILLISECONDS
    ).toLong()

    val requests = LongContinuousBuffer(info.permits)

    companion object {
        fun fromAll(rates: List<RateInfo>) = rates.map(::RateContainer)
    }
}