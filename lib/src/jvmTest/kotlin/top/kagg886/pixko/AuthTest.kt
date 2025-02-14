package top.kagg886.pixko

import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.logging.*
import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.module.illust.getRecommendIllust
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthTest {
    @Test
    fun testRandomVerify() {
        val account = PixivAccountFactory.newAccount(OkHttp)

        assertEquals(128, account.codeVerify.length)
    }

    @Test
    fun testChallenge(): Unit = runBlocking {
        //from Pixiv-Shaft
        val account = PixivAccountFactory.newAccount(OkHttp,"-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI")

        assertEquals("usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0", account.codeChallenge)
    }

    @Test
    fun testNewAuth(): Unit = runBlocking {
        val auth = PixivAccountFactory.newAccount(OkHttp)

        println(auth.url)

        println(auth.verify("pixiv://account/login?code=ywcsIE1E1ro9pGY9kRHQMfxKw4bMl2lDuDDRnvuze30&via=login"))
    }

    @Test
    fun testFailAccessTokenGen(): Unit = runBlocking {
        val client = PixivAccountFactory.newAccountFromConfig(OkHttp) {
            config = {
                engine {
                    config {
                        followRedirects(true)
                    }
                }
            }

            storage = InMemoryTokenStorage().apply {
                setToken(TokenType.ACCESS, "awa")
                setToken(TokenType.REFRESH, "qwq")
            }

            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }

            }
        }
        assertFailsWith(InvaidRefreshTokenException::class) {
            client.getRecommendIllust()
        }
    }

    companion object {
        fun generatePixivAccount() =
            PixivAccountFactory.newAccountFromConfig(OkHttp) {
                storage = InMemoryTokenStorage().apply {
                    setToken(TokenType.REFRESH, "xtkew_VEEQOxOW2xUeNE_Y8cX1g--Fhw9CtBAC6BVPQ")
                }
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }
    }

}
