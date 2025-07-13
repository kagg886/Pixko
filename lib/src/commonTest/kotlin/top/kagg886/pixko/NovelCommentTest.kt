package top.kagg886.pixko
import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.novel.deleteNovelComment
import top.kagg886.pixko.module.novel.getNovelComment
import top.kagg886.pixko.module.novel.getNovelCommentReply
import top.kagg886.pixko.module.novel.sendNovelComment
import kotlin.test.Test

class NovelCommentTest : TestWithClient() {
    @Test
    fun testCommentList() = runTest {
        val list = client.getNovelComment(21844391)
        println(list)
    }


    @Test
    fun testCommentReplyList() = runTest {
        val result = client.getNovelCommentReply(49294666)
        println(result)
    }

    @Test
    fun testSendComment() = runTest {
        val result = client.sendNovelComment {
            comment = "测试评论"
            novelId = 21844391
        }
        println(result)
    }

    @Test
    fun testSendCommentReply() = runTest {
        val result = client.sendNovelComment {
            comment = "测试回复"
            novelId = 21844391
            parentCommentId = 49294734
        }
        println(result)
    }

    @Test
    fun testCommentDelete() = runTest {
        val result = client.deleteNovelComment(49294742)
        println(result)
    }
}
