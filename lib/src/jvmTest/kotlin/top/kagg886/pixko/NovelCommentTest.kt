package top.kagg886.pixko
import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.module.novel.deleteNovelComment
import top.kagg886.pixko.module.novel.getNovelComment
import top.kagg886.pixko.module.novel.getNovelCommentReply
import top.kagg886.pixko.module.novel.sendNovelComment
import kotlin.test.BeforeTest
import kotlin.test.Test

class NovelCommentTest {
    @Test
    fun testCommentList(): Unit = runBlocking {
        val list = client.getNovelComment(21844391)
    }


    @Test
    fun testCommentReplyList(): Unit = runBlocking {
        val result = client.getNovelCommentReply(49294666)
        println(result)
    }

    @Test
    fun testSendComment(): Unit = runBlocking {
        val result = client.sendNovelComment {
            comment = "测试评论"
            novelId = 21844391
        }
        println(result)
    }

    @Test
    fun testSendCommentReply(): Unit = runBlocking {
        val result = client.sendNovelComment {
            comment = "测试回复"
            novelId = 21844391
            parentCommentId = 49294734
        }
        println(result)
    }

    @Test
    fun testCommentDelete(): Unit = runBlocking {
        val result = client.deleteNovelComment(49294742)
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
