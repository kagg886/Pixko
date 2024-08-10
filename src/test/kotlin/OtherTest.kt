import kotlinx.datetime.Instant
import kotlin.test.Test

class OtherTest {
    @Test
    fun testLocalDateTime() {
        println(Instant.parse("2022-07-17T13:16:49+09:00"))
    }
}