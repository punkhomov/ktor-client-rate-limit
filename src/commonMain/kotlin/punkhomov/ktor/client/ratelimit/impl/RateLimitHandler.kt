package punkhomov.ktor.client.ratelimit.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import punkhomov.ktor.client.ratelimit.core.RateInfo

class RateLimitHandler(rates: List<RateInfo>) {
    private val rates = RateContainer.fromAll(rates)
    private val mutex = Mutex()

    suspend fun handle() = mutex.withLock {
        val now = Clock.System.now().toEpochMilliseconds()

        val waitTime = rates.maxOfOrNull { rate ->
            // calc a time to wait in ms
            if (rate.requests.size < rate.permits) {
                0
            } else {
                val oldestPermit = rate.requests.oldest()
                val newestPermit = rate.requests.newest()

                if (newestPermit - oldestPermit > rate.period) {
                    0
                } else {
                    oldestPermit + rate.period - now // Remaining time
                }
            }
        }!!

        val estimatedTime = now + waitTime

        rates.forEach { rate ->
            rate.requests.add(estimatedTime)
        }

        if (waitTime > 0) {
            delay(waitTime)
        }
    }
}