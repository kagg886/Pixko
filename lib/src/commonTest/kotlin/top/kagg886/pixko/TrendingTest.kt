package top.kagg886.pixko
import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.trending.getRecommendTags
import kotlin.test.Test

class TrendingTest : TestWithClient() {
    @Test
    fun testTrendingIllust() = runTest {
        println(client.getRecommendTags())
    }
}
