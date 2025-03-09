package top.kagg886.pixko

import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.illust.deleteIllustComment
import top.kagg886.pixko.module.illust.getIllustComment
import top.kagg886.pixko.module.illust.getIllustCommentReply
import top.kagg886.pixko.module.illust.sendIllustComment
import kotlin.test.Test

class IllustCommentTest : TestWithClient() {
    @Test
    fun testCommentList() = runTest {
        val list = client.getIllustComment(120472631)
    }

    @Test
    fun testSendComment() = runTest {
        val result = client.sendIllustComment {
            comment = "测试评论"
            illustId = 120472631
        }
        println(result)
    }

    @Test
    fun testSendCommentReply() = runTest {
        val result = client.sendIllustComment {
            comment = "测试回复"
            illustId = 120472631
            parentCommentId = 181680577
        }
        println(result)
    }

    @Test
    fun testCommentDelete() = runTest {
        val result = client.deleteIllustComment(181680577)
        println(result)
    }

    @Test
    fun testCommentReplyList() = runTest {
        val result = client.getIllustCommentReply(181680577)
        println(result)
    }

}
