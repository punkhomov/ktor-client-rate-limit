package punkhomov.ktor.client.ratelimit.core

import kotlin.time.DurationUnit

data class Rate(
    /**
     * Number of permits for a given period.
     */
    val permits: Int,

    /**
     * The period in [periodUnit] units.
     */
    val periodTime: Long,

    /**
     * Period units.
     *
     * @see periodTime
     */
    val periodUnit: DurationUnit,
) {
    init {
        require(permits > 0) { "'permits' must be a positive number." }
        require(periodTime > 0) { "'periodTime' must be a positive number." }
        require(periodUnit != DurationUnit.NANOSECONDS) { "Nanoseconds are too small for delays." }
        require(periodUnit != DurationUnit.MICROSECONDS) { "Microseconds are too small for delays." }
    }
}