package top.kagg886.pixko

import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.module.illust.deleteIllustComment
import top.kagg886.pixko.module.illust.getIllustComment
import top.kagg886.pixko.module.illust.getIllustCommentReply
import top.kagg886.pixko.module.illust.sendIllustComment
import kotlin.test.BeforeTest
import kotlin.test.Test

class IllustCommentTest {
    @Test
    fun testCommentList(): Unit = runBlocking {
        val list = client.getIllustComment(120472631)
    }

    @Test
    fun testSendComment(): Unit = runBlocking {
        val result = client.sendIllustComment {
            comment = "测试评论"
            illustId = 120472631
        }
        println(result)
    }

    @Test
    fun testSendCommentReply(): Unit = runBlocking {
        val result = client.sendIllustComment {
            comment = "测试回复"
            illustId = 120472631
            parentCommentId = 181680577
        }
        println(result)
    }

    @Test
    fun testCommentDelete(): Unit = runBlocking {
        val result = client.deleteIllustComment(181680577)
        println(result)
    }

    @Test
    fun testCommentReplyList(): Unit = runBlocking {
        val result = client.getIllustCommentReply(181680577)
        println(result)
    }

    companion object {
        lateinit var client: PixivAccount

        @BeforeTest
        fun preparePixivClient() {
            client = AuthTest.generatePixivAccount()
        }
    }
}
