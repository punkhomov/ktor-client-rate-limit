package punkhomov.ktor.client.ratelimit.impl

import kotlinx.datetime.Clock

internal fun getNowAsEpochMilliseconds() =
    Clock.System.now().toEpochMilliseconds()

internal fun takePositiveOrZero(value: Long) =
    if (value > 0) value else 0

internal val zeroInit: (index: Int) -> Long =
    { 0 }