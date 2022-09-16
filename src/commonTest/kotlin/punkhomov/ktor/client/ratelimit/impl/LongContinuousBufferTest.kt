package punkhomov.ktor.client.ratelimit.impl

import kotlin.test.Test
import kotlin.test.assertContentEquals


internal class LongContinuousBufferTest {
    @Test
    fun testing_overflow__Expect_success() {
        val buffer = LongContinuousBuffer(5)

        repeat(13) {
            buffer.add(it.toLong())
        }

        assertContentEquals(listOf(8, 9, 10, 11, 12), buffer.toList())
    }
}