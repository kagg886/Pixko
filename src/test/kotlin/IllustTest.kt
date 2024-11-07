import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.*
import top.kagg886.pixko.module.loadImage
import top.kagg886.pixko.module.search.SearchSort
import top.kagg886.pixko.module.search.searchIllust
import kotlin.test.Test
import kotlin.test.assertTrue

class IllustTest {

    @Test
    fun testLoadImage(): Unit = runBlocking {
        val stream =
            client.loadImage("https://i.pximg.net/c/540x540_70/img-master/img/2024/08/10/16/13/07/121356207_p0_master1200.jpg")

        println(stream.size)
    }

    @Test
    fun testFollow(): Unit = runBlocking {
        println(client.getIllustFollowList())
    }

    @Test
    fun testR18GFilter():Unit = runBlocking {
        listOf(
            client.getIllustDetail(122433765)
        ). apply {
            println(this.joinToString("\n\n\n") { it.toString() })
        }
    }

    @Test
    fun testIllustRecommend(): Unit = runBlocking {
        for (i in 1..3) {
            val next = client.getRecommendIllust()
            for (j in next.illusts) {
                println(j)
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
        //120131445 一张图片
        //120585273 三张图片
        while (true) {
            try {
                val a = client.getIllustDetail(106340654)
                println(a)
                println(a.contentImages.get(IllustImagesType.ORIGIN))
            } catch (e: Exception) {
                continue
            }
            break
        }
    }

    @Test
    fun testBookmark(): Unit = runBlocking {
        assert(client.bookmarkIllust(85297928))
    }

    @Test
    fun testDeleteBookmark(): Unit = runBlocking {
        client.deleteBookmarkIllust(85297928)
    }

    @Test
    fun testLatestIllust(): Unit = runBlocking {
        client.getLatestIllust()
    }

    @Test
    fun testNonPremiumSearchMorePageEmpty(): Unit = runBlocking {
        val list = client.searchIllust("原神") {
            sort = SearchSort.POPULAR_DESC
            page = 2
        }
        assertTrue(list.isEmpty())
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