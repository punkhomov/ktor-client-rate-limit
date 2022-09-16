package punkhomov.ktor.client.ratelimit.core

import kotlin.time.DurationUnit

data class RateInfo(
    /**
     * Number of permits for a given [period].
     */
    val permits: Int,
    /**
     * The period in [unit] units.
     */
    val period: Int,
    /**
     * Period units.
     *
     * @see period
     */
    val unit: DurationUnit,
)