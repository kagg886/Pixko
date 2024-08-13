import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.novel.*
import kotlin.test.Test

class NovelTest {

    @Test
    fun testNovelRank(): Unit = runBlocking {
        val novel = client.getRankNovel(RankCategory.DAY)
        println(novel)
    }

    @Test
    fun testDetail():Unit = runBlocking {
        val novel = client.getNovelDetail( 22767441)
    }

    @Test
    fun testRecommendNovel(): Unit = runBlocking {
        val novel = client.getRecommendNovel()
        println(novel)
    }

    @Test
    fun testSeriesList(): Unit = runBlocking {
        val novel = client.getNovelSeries(9714240)
        println(novel)
    }

    @Test
    fun testNovelContent():Unit = runBlocking {

        //测试版本：22767441
        val novel = client.getNovelContent(22767441)
        println(novel)
    }

    @Test
    fun testNovelBookmark():Unit = runBlocking {
        client.bookmarkNovel(21844391)
    }

    @Test
    fun testNovelBookmarkDelete():Unit = runBlocking {
        val novel = client.deleteBookmarkNovel(21844391)
        println(novel)
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