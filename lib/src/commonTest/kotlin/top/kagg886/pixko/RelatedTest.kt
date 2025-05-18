package top.kagg886.pixko

import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.illust.getRelatedIllust
import top.kagg886.pixko.module.illust.getRelatedIllustNext
import top.kagg886.pixko.module.novel.getRelatedNovel
import top.kagg886.pixko.module.novel.getRelatedNovelNext
import top.kagg886.pixko.module.user.getRelatedUser
import top.kagg886.pixko.module.user.getRelatedUserNext
import kotlin.test.Test

class RelatedTest: TestWithClient() {
    @Test
    fun testIllustRelated() = runTest {
        val result = client.getRelatedIllust(130287585)
        println(result)
        val result1 = client.getRelatedIllustNext(result)
        println(result1)
    }
    @Test
    fun testNovelRelated() = runTest {
        val result = client.getRelatedNovel(20510070)
        println(result)

        val result1 = client.getRelatedNovelNext(result)
        println(result1)
    }
    @Test
    fun testUserRelated() = runTest {
        val result = client.getRelatedUser(82157783)
        println(result)
        val result1 = client.getRelatedUserNext(result)
        println(result1)
    }
}
