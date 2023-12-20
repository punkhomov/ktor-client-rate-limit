package punkhomov.ktor.client.ratelimit.impl

import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import punkhomov.ktor.client.ratelimit.core.Rate

class MultiRateLimitHandler(rates: List<Rate>) {
    private val rateHolders = createHoldersFrom(rates)
    private val mutex = Mutex()

    suspend fun handle() = mutex.withLock {
        val startTime = getNowAsEpochMilliseconds()
        val waitTime = getWaitTimeForMostDelayedRate(startTime)
        if (waitTime > 0) {
            delay(waitTime)
        }

        val actualPermitTime = getNowAsEpochMilliseconds()
        permitToAllRateHoldersWithTime(actualPermitTime)
    }

    private fun getWaitTimeForMostDelayedRate(now: Long): Long {
        return rateHolders.maxOf { holder -> calcWaitTimeForHolder(now, holder) }
    }

    private fun calcWaitTimeForHolder(now: Long, holder: RateHolder): Long {
        val boundaryTimeForNewPermit = holder.oldestPermit() + holder.period
        val diffTime = boundaryTimeForNewPermit - now
        return takePositiveOrZero(diffTime)
    }

    private fun permitToAllRateHoldersWithTime(time: Long) {
        rateHolders.forEach { rate -> rate.permitted.add(time) }
    }
}