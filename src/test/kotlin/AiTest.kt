import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.ai.isAllowedAiShow
import top.kagg886.pixko.module.ai.setAllowedAiShow
import kotlin.test.Test

class AiTest {

    @Test
    fun testAiShow():Unit = runBlocking {
        println(client.isAllowedAiShow())
    }

    @Test
    fun testAiShowSetting():Unit = runBlocking {
        println(client.setAllowedAiShow(false))
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