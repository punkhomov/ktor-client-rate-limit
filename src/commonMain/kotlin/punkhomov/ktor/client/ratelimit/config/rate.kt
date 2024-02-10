package punkhomov.ktor.client.ratelimit.config

import punkhomov.ktor.client.ratelimit.ExperimentalRateLimit
import punkhomov.ktor.client.ratelimit.core.Rate
import punkhomov.ktor.client.ratelimit.core.RateLimitRule
import punkhomov.ktor.client.ratelimit.impl.DefaultRateLimitRule
import kotlin.jvm.JvmInline
import kotlin.time.Duration
import kotlin.time.DurationUnit

fun DefaultRateLimitRuleBuilder.rate(vararg rates: Rate): RateLimitRule {
    check(rates.isNotEmpty()) { "Empty rate. Please add at least one rate." }
    return DefaultRateLimitRule(matcher, keySelector, rates.toList())
}

@ExperimentalRateLimit
@JvmInline
value class Permits(val permits: Int)

@ExperimentalRateLimit
inline val Int.permits get() = Permits(this)

@ExperimentalRateLimit
infix fun Permits.per(duration: Duration): Rate {
    return Rate(permits, duration.toLong(DurationUnit.MILLISECONDS), DurationUnit.MILLISECONDS)
}