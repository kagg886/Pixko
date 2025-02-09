
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.trending.getRecommendTags
import kotlin.test.Test

class TrendingTest {

    @Test
    fun testTrendingIllust(): Unit = runBlocking {
        println(client.getRecommendTags())
    }

    companion object {
        lateinit var client: PixivAccount

        
        @BeforeTest
        fun preparePixivClient() {
            client = AuthTest.generatePixivAccount()
        }
    }
}