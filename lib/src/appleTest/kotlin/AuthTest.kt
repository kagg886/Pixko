import io.ktor.client.engine.darwin.*
import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.InMemoryTokenStorage
import top.kagg886.pixko.InvaidRefreshTokenException
import top.kagg886.pixko.PixivAccountFactory
import top.kagg886.pixko.TokenType
import top.kagg886.pixko.module.illust.getRecommendIllust
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AuthTest {
    @Test
    fun testRandomVerify() {
        val account = PixivAccountFactory.newAccount(Darwin)

        assertEquals(128, account.codeVerify.length)
    }

    @Test
    fun testChallenge(): Unit = runBlocking {
        //from Pixiv-Shaft
        val account = PixivAccountFactory.newAccount(Darwin,"-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI")

        assertEquals("usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0", account.codeChallenge)
    }

    @Test
    fun testNewAuth(): Unit = runBlocking {
        val auth = PixivAccountFactory.newAccount(Darwin)

        println(auth.url)

        println(auth.verify("pixiv://account/login?code=ywcsIE1E1ro9pGY9kRHQMfxKw4bMl2lDuDDRnvuze30&via=login"))
    }

    @Test
    fun testFailAccessTokenGen(): Unit = runBlocking {
        val client = PixivAccountFactory.newAccountFromConfig(Darwin) {
            config = {
                engine {

                }
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
            PixivAccountFactory.newAccountFromConfig(Darwin) {
                storage = InMemoryTokenStorage().apply {
                    setToken(TokenType.REFRESH, "xtkew_VEEQOxOW2xUeNE_Y8cX1g--Fhw9CtBAC6BVPQ")
                }
            }
    }

}
