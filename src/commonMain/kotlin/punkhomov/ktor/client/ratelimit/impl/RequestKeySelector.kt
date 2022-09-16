package punkhomov.ktor.client.ratelimit.impl

import io.ktor.client.request.*

fun interface RequestKeySelector {
    operator fun invoke(request: HttpRequestBuilder): RequestKey
}


class NoRequestKeySelector : RequestKeySelector {
    private val requestKey = Any()

    override fun invoke(request: HttpRequestBuilder): RequestKey {
        return requestKey
    }
}


typealias RequestKey = Any