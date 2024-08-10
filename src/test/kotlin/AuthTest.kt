import top.kagg886.pixko.PixivAccountFactory
import kotlinx.coroutines.runBlocking
import kotlin.test.Test

class AuthTest {
    @Test
    fun testNewAuth():Unit = runBlocking {
        val auth = PixivAccountFactory.newAccount()

        println(auth.url())

        println(auth.verify("pixiv://account/login?code=1&via=login"))
    }
}