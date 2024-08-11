import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.user.getCurrentUserSimpleProfile
import top.kagg886.pixko.module.user.getUserInfo
import kotlin.test.Test

class UserTest {

    @Test
    fun testProfile():Unit = runBlocking {
        val state = client.getCurrentUserSimpleProfile()
        println(state)
    }

    @Test
    fun testUserDetail():Unit = runBlocking {
        val state = client.getUserInfo(38000473)
        println(state)
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