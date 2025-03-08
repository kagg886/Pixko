package top.kagg886.pixko

import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.novel.*
import top.kagg886.pixko.module.novel.parser.UploadImageNode
import kotlin.test.Test

class NovelTest : TestWithClient() {

    @Test
    fun testNovelRank() = runTest {
        val novel = client.getRankNovel(RankCategory.DAY)
        println(novel)
    }

    @Test
    fun testDetail() = runTest {
        val novel = client.getNovelDetail(22767441)
    }

    @Test
    fun testRecommendNovel() = runTest {
        val novel = client.getRecommendNovel()
        val novel1 = client.getRecommendNovelNext(novel)
        println(novel)
        println(novel1)
    }

    @Test
    fun testSeriesList() = runTest {
        val novel = client.getNovelSeries(9714240)
        println(novel)
    }

    @Test
    fun testNovelContent() = runTest {

        //测试版本：22767441
        val novel = client.getNovelContent(23351531)
        println(novel.data.filterIsInstance<UploadImageNode>().map { it.url })
    }

    @Test
    fun testNovelBookmark() = runTest {
        client.bookmarkNovel(21844391)
    }

    @Test
    fun testNovelBookmarkDelete() = runTest {
        val novel = client.deleteBookmarkNovel(21844391)
        println(novel)
    }

}
