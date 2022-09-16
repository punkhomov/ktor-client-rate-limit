package punkhomov.ktor.client.ratelimit.impl

import io.ktor.client.request.*

fun interface RequestMatcher {
    operator fun invoke(request: HttpRequestBuilder): Boolean
}


class NoRequestMatcher : RequestMatcher {
    override fun invoke(request: HttpRequestBuilder): Boolean {
        return true
    }
}


class RequestMatcherAnd(
    val operand1: RequestMatcher,
    val operand2: RequestMatcher
) : RequestMatcher {
    override fun invoke(request: HttpRequestBuilder): Boolean {
        return operand1(request) && operand2(request)
    }
}

infix fun RequestMatcher.and(other: RequestMatcher) = RequestMatcherAnd(this, other)


class RequestMatcherNot(
    val operand: RequestMatcher
) : RequestMatcher {
    override fun invoke(request: HttpRequestBuilder): Boolean {
        return operand(request).not()
    }
}

fun RequestMatcher.not() = RequestMatcherNot(this)