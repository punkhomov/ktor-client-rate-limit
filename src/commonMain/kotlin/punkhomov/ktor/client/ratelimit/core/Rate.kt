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
)