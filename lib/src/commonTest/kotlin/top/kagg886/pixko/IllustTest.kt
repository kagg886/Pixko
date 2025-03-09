package top.kagg886.pixko

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.utils.io.*
import kotlinx.coroutines.test.runTest
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import top.kagg886.pixko.module.illust.*
import top.kagg886.pixko.module.search.SearchSort
import top.kagg886.pixko.module.search.searchIllust
import top.kagg886.pixko.module.ugoira.getUgoiraMetadata
import kotlin.test.Test
import kotlin.test.assertTrue

class IllustTest : TestWithClient() {
    @Test
    fun testFollow() = runTest {
        println(client.getIllustFollowList())
    }

    @Test
    fun testR18GFilter() = runTest {
        listOf(
            client.getIllustDetail(122433765)
        ).apply {
            println(this.joinToString("\n\n\n") { it.toString() })
        }
    }

    @Test
    fun testIllustRecommend() = runTest {
        for (i in 1..3) {
            val next = client.getRecommendIllust()
            for (j in next.illusts) {
                println(j)
            }
        }
    }

    @Test
    fun testRank() = runTest {
        for (i in 1..3) {
            client.getRankIllust(RankCategory.ORIGINAL, i).apply {
                println(this)
            }
        }
    }

    @Test
    fun testIllustDetail() = runTest {
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
    fun testUgoiraIllust() = runTest {
        //a ugoira was built by a zip contained the frame.
        val a = client.getIllustDetail(126473222)
        val b = client.getUgoiraMetadata(a)
        val channel = client.client.get(b.url.content).body<ByteReadChannel>()
        println(b)

        val path = Path("a.zip")

        if (SystemFileSystem.exists(path)) {
            SystemFileSystem.delete(path)
        }


        val sink = SystemFileSystem.sink(path)

        channel.readBuffer().use {
            sink.write(it, it.size)
        }

    }

    @Test
    fun testBookmark() = runTest {
        assertTrue(client.bookmarkIllust(85297928))
    }

    @Test
    fun testBookmarkWithTags() = runTest {
        val illust = client.getIllustDetail(85297928)
        assertTrue(
            client.bookmarkIllust(85297928) {
                tags = illust.tags
            }
        )
    }

    @Test
    fun testDeleteBookmark() = runTest {
        client.deleteBookmarkIllust(85297928)
    }

    @Test
    fun testLatestIllust() = runTest {
        client.getLatestIllust()
    }

    @Test
    fun testNonPremiumSearchMorePageEmpty() = runTest {
        val list = client.searchIllust("原神") {
            sort = SearchSort.POPULAR_DESC
            page = 2
        }
        assertTrue(list.isEmpty())
    }
}
