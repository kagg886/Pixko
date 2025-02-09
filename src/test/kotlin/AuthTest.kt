import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.*
import top.kagg886.pixko.module.illust.getRecommendIllust
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
    @Test
    fun testChallenge(): Unit = runBlocking {
        //from Pixiv-Shaft
        val account = PixivAccountFactory.newAccount("-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI")

        val prop = account::class.memberProperties.find { it.name == "code_challenge" }!!
        prop.isAccessible = true
        assertEquals("usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0", prop.call(account))
    }

    @Test
    fun testNewAuth(): Unit = runBlocking {
        val auth = PixivAccountFactory.newAccount()

//        println(auth.url)

        println(auth.verify("pixiv://account/login?code=ywcsIE1E1ro9pGY9kRHQMfxKw4bMl2lDuDDRnvuze30&via=login"))
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