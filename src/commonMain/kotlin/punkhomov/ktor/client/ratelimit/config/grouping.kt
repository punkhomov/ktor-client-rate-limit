package punkhomov.ktor.client.ratelimit.config

import punkhomov.ktor.client.ratelimit.impl.RequestKeySelector

fun DefaultRateLimitRuleBuilder.distributeBy(keySelector: RequestKeySelector): DefaultRateLimitRuleBuilder {
    return this.apply { this.keySelector = keySelector }
}