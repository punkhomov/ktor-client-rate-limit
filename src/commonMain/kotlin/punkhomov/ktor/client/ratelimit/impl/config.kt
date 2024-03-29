@file:Suppress("DEPRECATION")

package punkhomov.ktor.client.ratelimit.impl

import punkhomov.ktor.client.ratelimit.*
import kotlin.time.DurationUnit

class RateLimitRuleBuilder(val config: RateLimit.Config) {
    var matcher: RequestMatcher = NoRequestMatcher()
    var keySelector: RequestKeySelector = NoRequestKeySelector()
}


interface RateLimitRuleBuilderWrapper {
    val builder: RateLimitRuleBuilder
}

class RequestMatcherWrapping(
    override val builder: RateLimitRuleBuilder
) : RateLimitRuleBuilderWrapper

class RequestGrouperWrapping(
    override val builder: RateLimitRuleBuilder
) : RateLimitRuleBuilderWrapper


/**
 * Matches requests by the given [matcher] function.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "rule(select(matcher)...)",
        imports = ["punkhomov.ktor.client.ratelimit.config.select"]
    )
)
fun RateLimit.Config.select(
    matcher: RequestMatcher
): RequestMatcherWrapping = RequestMatcherWrapping(
    RateLimitRuleBuilder(this).apply {
        this.matcher = matcher
    }
)

/**
 * Matches any given request (aka `else` branch in `when`).
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "rule(anyRequest()...)",
        imports = ["punkhomov.ktor.client.ratelimit.config.anyRequest()"]
    )
)
fun RateLimit.Config.default() =
    select { true }

/**
 * Matches requests whose host equals the given [host] value.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "rule(ifHostEquals(host))",
        imports = ["punkhomov.ktor.client.ratelimit.config.ifHostEquals"]
    )
)
fun RateLimit.Config.withHost(host: String) =
    select { it.url.host == host }

/**
 * Matches requests whose url starts with the given [urlPrefix] value.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "rule(ifUrlStartsWith(urlPrefix))",
        imports = ["punkhomov.ktor.client.ratelimit.config.ifUrlStartsWith"]
    )
)
fun RateLimit.Config.urlStartsWith(urlPrefix: String) =
    select { it.url.buildString().startsWith(urlPrefix) }

/**
 * Matches requests whose host ends with the given [hostPostfix] value.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "rule(ifHostEndsWith(hostPostfix))",
        imports = ["punkhomov.ktor.client.ratelimit.config.ifHostEndsWith"]
    )
)
fun RateLimit.Config.hostEndsWith(hostPostfix: String) =
    select { it.url.host.endsWith(hostPostfix) }


/**
 * Excludes requests from matching if the given [matcher] return true.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "exclude(matcher)",
        imports = ["punkhomov.ktor.client.ratelimit.config.exclude"]
    )
)
fun RequestMatcherWrapping.exclude(
    matcher: RequestMatcher
): RequestMatcherWrapping = apply {
    builder.matcher = RequestMatcherAnd(builder.matcher, RequestMatcherNot(matcher))
}

/**
 * Excludes requests from matching if their host is in the [hosts] list.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    replaceWith = ReplaceWith(
        expression = "excludeHostIn(hosts)",
        imports = ["punkhomov.ktor.client.ratelimit.config.excludeHostIn"]
    )
)
fun RequestMatcherWrapping.excludeHosts(
    vararg hosts: String
): RequestMatcherWrapping = apply {
    builder.matcher = RequestMatcherAnd(builder.matcher, RequestMatcherNotInHosts(hosts.toList()))
}

/**
 * Excludes requests from matching if their path is in the [paths] list.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    ReplaceWith(
        expression = "exclude(RequestMatcherNotInPaths(paths.toList()))",
        imports = [
            "punkhomov.ktor.client.ratelimit.config.exclude",
            "punkhomov.ktor.client.ratelimit.impl.RequestMatcherNotInPaths",
        ]
    ),
)
fun RequestMatcherWrapping.excludePaths(
    vararg paths: String
): RequestMatcherWrapping = apply {
    builder.matcher = RequestMatcherAnd(builder.matcher, RequestMatcherNotInPaths(paths.toList()))
}


/**
 * Groups requests by the key returned by the given [keySelector]. These groups
 * independently delay requests.
 *
 * Example:
 * ```
 * // default request key for "unauthorized" requests
 * NO_TOKEN = Any()
 * // requests which "api_token" keys independent of each other will be rated independently
 * groupBy { request -> request.cookies["api_token"] ?: NO_TOKEN }
 * ```
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
    ReplaceWith(
        expression = "distributeBy(keySelector)",
        imports = ["punkhomov.ktor.client.ratelimit.config.distributeBy"]
    ),
)
fun RequestMatcherWrapping.groupBy(
    keySelector: RequestKeySelector
): RequestGrouperWrapping = RequestGrouperWrapping(
    builder.apply {
        this.keySelector = keySelector
    }
)


/**
 * Sets the rate.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
)
fun RateLimitRuleBuilderWrapper.rate(block: MultiRateBuilder.() -> Unit) {
    val rules = MultiRateBuilder().apply(block).toRates()
    builder.config.rule(DefaultRateLimitRule(builder.matcher, builder.keySelector, rules))
}

/**
 * Sets the rate.
 */
@Deprecated(
    message = "Avoid using unclear configurations, rule(...) should be used.",
)
fun RateLimitRuleBuilderWrapper.rate(
    permits: Int,
    period: Long,
    unit: DurationUnit,
) = rate { add(permits, period, unit) }