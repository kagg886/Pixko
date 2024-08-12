
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.*
import top.kagg886.pixko.module.search.searchIllust
import kotlin.test.Test

class IllustTest {
    @Test
    fun testIllustRecommend(): Unit = runBlocking {
        for (i in 1..3) {
            val next = client.getRecommendIllust()
            println(next)
        }
    }

    @Test
    fun testSearch(): Unit = runBlocking {
        for (i in 1..3) {
            client.searchIllust("原神").apply {
                println(this.illusts)
            }
        }
    }

    @Test
    fun testRank(): Unit = runBlocking {
        for (i in 1..3) {
            client.getRankIllust(RankCategory.ORIGINAL, i).apply {
                println(this)
            }
        }
    }

    @Test
    fun testIllustDetail(): Unit = runBlocking {
        val k = client.getIllustDetail(85297928)
        println(k)
    }

    @Test
    fun testBookmark():Unit = runBlocking {
        assert(client.bookmarkIllust(85297928))
    }

    @Test
    fun testDeleteBookmark():Unit = runBlocking {
        client.deleteBookmarkIllust(85297928)
    }

    companion object {
        lateinit var client: PixivAccount

        @JvmStatic
        @BeforeAll
        fun preparePixivClient() {
            client = AuthTest.generatePixivAccount()
        }
    }
}