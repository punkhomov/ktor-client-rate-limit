package punkhomov.ktor.client.ratelimit.config

import punkhomov.ktor.client.ratelimit.impl.NoRequestKeySelector
import punkhomov.ktor.client.ratelimit.impl.NoRequestMatcher
import punkhomov.ktor.client.ratelimit.impl.RequestKeySelector
import punkhomov.ktor.client.ratelimit.impl.RequestMatcher

class DefaultRateLimitRuleBuilder {
    var matcher: RequestMatcher = NoRequestMatcher()
    var keySelector: RequestKeySelector = NoRequestKeySelector()
}