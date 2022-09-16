package punkhomov.ktor.client.ratelimit.core

import io.ktor.client.request.*

interface RateLimitRule {
    /**
     * The list of rates.
     */
    val rates: List<RateInfo>

    /**
     * The function checks if the given request matches this rule and should be handled.
     *
     * This function called before the given request has sent.
     *
     * @param request The given request.
     * @return `true` if the given request should be handled by this rule, `false`
     * otherwise.
     */
    fun isMatch(request: HttpRequestBuilder): Boolean

    /**
     * The function delays sending the given request. You have to use
     * [kotlinx.coroutines.delay] function to delaying.
     *
     * This function called before the given request has sent.
     *
     * @param request The request that matches this rule
     */
    suspend fun onSendRequest(request: HttpRequestBuilder)
}