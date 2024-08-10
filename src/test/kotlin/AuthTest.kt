import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.InMemoryTokenStorage
import top.kagg886.pixko.PixivAccountConfig
import top.kagg886.pixko.PixivAccountFactory
import top.kagg886.pixko.TokenType
import kotlin.test.Test

class AuthTest {
    @Test
    fun testNewAuth(): Unit = runBlocking {
        val auth = PixivAccountFactory.newAccount()

        println(auth.url())

        println(auth.verify("pixiv://account/login?code=1&via=login"))
    }

    companion object {
        fun generatePixivAccount() =
            PixivAccountFactory.newAccountFromConfig {
                storage = InMemoryTokenStorage().apply {
                    setToken(TokenType.ACCESS, "Btz1IKo5SumN1APrEBLuKzDzAS6S_q55Pfo_r-wvI7I")
                    setToken(TokenType.REFRESH, "xtkew_VEEQOxOW2xUeNE_Y8cX1g--Fhw9CtBAC6BVPQ")
                }

                logger = PixivAccountConfig.LoggerProprieties(
                    LogLevel.BODY, object : Logger {
                        override fun log(message: String) {
                            println(message)
                        }
                    }
                )
            }
    }

}