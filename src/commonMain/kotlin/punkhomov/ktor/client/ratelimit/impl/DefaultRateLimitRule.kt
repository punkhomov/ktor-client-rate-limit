package punkhomov.ktor.client.ratelimit.impl

import io.ktor.client.request.*
import io.ktor.util.collections.*
import punkhomov.ktor.client.ratelimit.core.RateInfo
import punkhomov.ktor.client.ratelimit.core.RateLimitRule

class DefaultRateLimitRule(
    val matcher: RequestMatcher,
    val keySelector: RequestKeySelector,
    override val rates: List<RateInfo>,
) : RateLimitRule {
    private val groupHandlers = ConcurrentMap<RequestKey, RateLimitHandler>()

    override fun isMatch(request: HttpRequestBuilder): Boolean {
        return matcher(request)
    }

    override suspend fun onSendRequest(request: HttpRequestBuilder) {
        groupHandlers.computeIfAbsent(keySelector(request)) {
            RateLimitHandler(rates)
        }.handle()
    }
}