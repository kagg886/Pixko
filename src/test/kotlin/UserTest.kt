import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.IllustResult
import top.kagg886.pixko.module.user.*
import java.util.*
import kotlin.test.Test

class UserTest {

    @Test
    fun testProfile(): Unit = runBlocking {
        val state = client.getCurrentUserSimpleProfile()
        println(state)
    }

    @Test
    fun testUserDetail(): Unit = runBlocking {
        val state = client.getUserInfo(38000473)
        println(state)
    }

    @Test
    fun testUserWorkSpaceEdit(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        val workspace = client.getUserInfo(userId).workspace

        client.setUserWorkSpace(workspace.copy(pc = UUID.randomUUID().toString()))
    }

    @Test
    fun testBookmarkList(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        var a: IllustResult? = client.getUserLikeIllust(userId)
        while (a != null) {
            a = client.getUserLikeIllustNext(a) ?: break
            println(a)
        }
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