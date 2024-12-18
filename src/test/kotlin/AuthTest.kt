import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.*
import top.kagg886.pixko.module.illust.getRecommendIllust
import kotlin.test.Test

class AuthTest {
    @Test
    fun testNewAuth(): Unit = runBlocking {
        val auth = PixivAccountFactory.newAccount()

        println(auth.url)

//        println(auth.verify("pixiv://account/login?code=1P6cBiQvbiGYz0MXslyQ3LdJS0lMkZNXyoCip3U462Q&via=login"))
    }

    @Test
    fun testFailAccessTokenGen(): Unit = runBlocking {
        val client = PixivAccountFactory.newAccountFromConfig {
            storage = InMemoryTokenStorage().apply {
                setToken(TokenType.ACCESS, "awa")
                setToken(TokenType.REFRESH, "qwq")
            }

            loggerLevel = PixivAccountConfig.LoggerLevel.ALL
        }
        val t = kotlin.runCatching {
            client.getRecommendIllust()
        }.exceptionOrNull()!! as PixivException
        println(t)
    }

    companion object {
        fun generatePixivAccount() =
            PixivAccountFactory.newAccountFromConfig {
                storage = InMemoryTokenStorage().apply {
                    setToken(TokenType.REFRESH, "xtkew_VEEQOxOW2xUeNE_Y8cX1g--Fhw9CtBAC6BVPQ")
                }

                loggerLevel = PixivAccountConfig.LoggerLevel.ALL
            }
    }

}