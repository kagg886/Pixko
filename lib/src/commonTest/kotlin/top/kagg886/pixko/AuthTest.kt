package top.kagg886.pixko

import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.illust.getRecommendIllust
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.seconds

class AuthTest {
    @Test
    fun testRandomVerify() {
        val account = TestPixivAccountFactory.newAccount()

        assertEquals(128, account.codeVerify.length)
    }

    @Test
    fun testChallenge() = runTest(timeout = 30.seconds) {
        //from Pixiv-Shaft
        val account = TestPixivAccountFactory.newAccount("-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI")
        assertEquals("usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0", account.codeChallenge)
    }

    @Test
    fun testNewAuth() = runTest {
        val auth = TestPixivAccountFactory.newAccount()

        println(auth.url)

        println(auth.verify("pixiv://account/login?code=ywcsIE1E1ro9pGY9kRHQMfxKw4bMl2lDuDDRnvuze30&via=login"))
    }

    @Test
    fun testFailAccessTokenGen() = runTest {
        val client = TestPixivAccountFactory.newAccountFromConfig {
            config = {
                followRedirects = true
            }

            storage = InMemoryTokenStorage().apply {
                setToken(TokenType.ACCESS, "awa")
                setToken(TokenType.REFRESH, "qwq")
            }
        }
        assertFailsWith(InvaidRefreshTokenException::class) {
            client.getRecommendIllust()
        }
    }

    companion object {
        fun generatePixivAccount() =
            TestPixivAccountFactory.newAccountFromConfig {
                storage = InMemoryTokenStorage().apply {
                    setToken(TokenType.REFRESH, "xtkew_VEEQOxOW2xUeNE_Y8cX1g--Fhw9CtBAC6BVPQ")
                }
            }
    }
}
