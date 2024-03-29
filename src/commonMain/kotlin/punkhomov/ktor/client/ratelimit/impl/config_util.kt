package punkhomov.ktor.client.ratelimit.impl

import io.ktor.client.request.*
import io.ktor.http.*
import punkhomov.ktor.client.ratelimit.core.Rate
import kotlin.time.DurationUnit

class RequestMatcherNotInPaths(val paths: List<String>) : RequestMatcher {
    override fun invoke(request: HttpRequestBuilder): Boolean {
        return request.url.encodedPath !in paths
    }
}

class RequestMatcherNotInHosts(val hosts: List<String>) : RequestMatcher {
    override fun invoke(request: HttpRequestBuilder): Boolean {
        return request.url.host !in hosts
    }
}


class MultiRateBuilder {
    private val rates = ArrayList<Rate>()

    fun add(rate: Rate) {
        rates.add(rate)
    }

    fun add(
        permits: Int,
        period: Long,
        unit: DurationUnit,
    ) = add(Rate(permits, period, unit))

    internal fun toRates() = rates.toList()
}