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
    fun testNovelContent():Unit = runBlocking {

        //测试版本：22767441
        val novel = client.getNovelContent(22767441)
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