package punkhomov.ktor.client.ratelimit.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import punkhomov.ktor.client.ratelimit.core.Rate
import kotlin.test.Test
import kotlin.time.DurationUnit
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlin.test.assertTrue


internal class RateLimitHandlerTest {
    @Test
    fun test_single_rate() = runTest {
        val clock = TestingClock()

        val rates = listOf(
            Rate(3, 1, DurationUnit.SECONDS),
        )
        val handler = MultiRateLimitHandler(rates)
        val permittedTimes = ArrayList<Long>()

        clock.markStart()
        repeat(10) {
            withContext(Dispatchers.Default) {
                handler.handle()
                permittedTimes.add(clock.getTime())
                if (it == 6) {
                    delay(2000)
                }
            }
        }

        Helper.printPermits(permittedTimes)
        Helper.assertTimesForRates(permittedTimes, rates)
    }

    @Test
    fun test_multi_rate() = runTest {
        val clock = TestingClock()

        val rates = listOf(
            Rate(3, 1, DurationUnit.SECONDS),
            Rate(10, 5, DurationUnit.SECONDS),
        )
        val handler = MultiRateLimitHandler(rates)
        val permittedTimes = ArrayList<Long>()

        clock.markStart()
        repeat(15) {
            withContext(Dispatchers.Default) {
                handler.handle()
                permittedTimes.add(clock.getTime())
            }
        }

        Helper.printPermits(permittedTimes)
        Helper.assertTimesForRates(permittedTimes, rates)
    }
}


private class TestingClock {
    var start = 0L

    fun markStart() { start = getNow() }

    fun getTime() = getNow() - start

    private fun getNow() = Clock.System.now().toEpochMilliseconds()
}

private object Helper {
     fun printPermits(timestamps: List<Long>) {
        val maximumIndicesSize = timestamps.lastIndex.toString().length
        val maximumTimestampsSize = timestamps.last().toString().length

        timestamps.forEachIndexed { index, timestamp ->
            val indexAligned = index.toString().padStart(maximumIndicesSize, ' ')
            val timestampAligned = timestamp.toString().padStart(maximumTimestampsSize, ' ')
            println("$indexAligned -> $timestampAligned")
        }
    }

    fun assertTimesForRates(timestamps: List<Long>, rates: List<Rate>) {
        val holders = createHoldersFrom(rates)
        holders.forEach { holder ->
            assertTimesForRateHolder(holder, timestamps)
        }
    }

    private fun assertTimesForRateHolder(holder: RateHolder, timestamps: List<Long>) {
        val permitsPerPeriod = holder.permitsPerPeriod
        val periodInMs = holder.period
        if (timestamps.size > permitsPerPeriod) {
            assertTimesInPeriod(timestamps, permitsPerPeriod, periodInMs)
        }
    }

    private fun assertTimesInPeriod(timestamps: List<Long>, permitsPerPeriod: Int, periodInMs: Long) {
        for (i in 0 until (timestamps.size - permitsPerPeriod)) {
            val oldest = timestamps[i]
            val newest = timestamps[i + permitsPerPeriod]

            assertTrue { newest - oldest > periodInMs }
        }
    }
}