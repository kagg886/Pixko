import top.kagg886.pixko.InMemoryTokenStorage
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.PixivAccountFactory
import top.kagg886.pixko.TokenType
import top.kagg886.pixko.module.illust.getRecommendIllust
import top.kagg886.pixko.module.illust.searchIllust
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test

class IllustTest {
    @Test
    fun testIllustRecommend(): Unit = runBlocking {
        for (i in 1..3) {
            val next = client.getRecommendIllust()
            println(next)
        }
    }

    @Test
    fun testSearch(): Unit = runBlocking {
        for (i in 1..3) {
            client.searchIllust("原神", page = i).apply {
                println(this.illusts)
            }
        }
    }

    companion object {
        lateinit var client: PixivAccount

        @JvmStatic
        @BeforeAll
        fun preparePixivClient() {
            client = PixivAccountFactory.newAccountFromConfig {
                storage = InMemoryTokenStorage().apply {
                    setToken(TokenType.ACCESS, "Btz1IKo5SumN1APrEBLuKzDzAS6S_q55Pfo_r-wvI7I")
                    setToken(TokenType.REFRESH, "xtkew_VEEQOxOW2xUeNE_Y8cX1g--Fhw9CtBAC6BVPQ")
                }

//                logger = PixivAccountConfig.LoggerProprieties(
//                    LogLevel.BODY, object : Logger {
//                        override fun log(message: String) {
//                            println(message)
//                        }
//                    }
//                )
            }
        }
    }
}