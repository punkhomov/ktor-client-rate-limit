package punkhomov.ktor.client.ratelimit.config

import io.ktor.http.*
import punkhomov.ktor.client.ratelimit.RateLimit
import punkhomov.ktor.client.ratelimit.impl.RequestMatcher
import punkhomov.ktor.client.ratelimit.impl.RequestMatcherAnd
import punkhomov.ktor.client.ratelimit.impl.RequestMatcherNot

@Suppress("UnusedReceiverParameter")
fun RateLimit.Config.select(matcher: RequestMatcher): DefaultRateLimitRuleBuilder {
    return DefaultRateLimitRuleBuilder().apply {
        this.matcher = matcher
    }
}

fun RateLimit.Config.ifHostEquals(host: String): DefaultRateLimitRuleBuilder {
    return select { it.url.host == host }
}

fun RateLimit.Config.ifHostIn(vararg hosts: String): DefaultRateLimitRuleBuilder {
    return select { it.url.host in hosts }
}

fun RateLimit.Config.ifHostEndsWith(suffix: String): DefaultRateLimitRuleBuilder {
    return select { it.url.host.endsWith(suffix) }
}

fun RateLimit.Config.ifUrlStartsWith(prefix: String): DefaultRateLimitRuleBuilder {
    return select { it.url.buildString().startsWith(prefix) }
}


fun DefaultRateLimitRuleBuilder.exclude(matcher: RequestMatcher): DefaultRateLimitRuleBuilder {
    return this.apply { this.matcher = RequestMatcherAnd(this.matcher, RequestMatcherNot(matcher)) }
}

fun DefaultRateLimitRuleBuilder.excludeEquals(host: String): DefaultRateLimitRuleBuilder {
    return exclude { it.url.host == host }
}

fun DefaultRateLimitRuleBuilder.excludeHostIn(vararg hosts: String): DefaultRateLimitRuleBuilder {
    return exclude { it.url.host in hosts }
}

fun DefaultRateLimitRuleBuilder.excludeHostStartsWith(prefix: String): DefaultRateLimitRuleBuilder {
    return exclude { it.url.host.startsWith(prefix) }
}

fun DefaultRateLimitRuleBuilder.excludePathStartsWith(prefix: String): DefaultRateLimitRuleBuilder {
    return exclude { it.url.encodedPath.startsWith(prefix) }
}


fun RateLimit.Config.anyRequest(): DefaultRateLimitRuleBuilder {
    return select { true }
}