package top.kagg886.pixko


import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.PixivAccountFactory.newAccount
import top.kagg886.pixko.PixivAccountFactory.newAccountFromConfig

//从APK中提取，不解释
internal const val pixiv_client_id = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
internal const val pixiv_client_secret = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"

/**
 * # Pixiv账号登陆器
 * 用于产生验证配置。
 * @see newAccount
 * @see newAccountFromConfig
 */
object PixivAccountFactory {

    internal const val code_verify = "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI"
    internal const val code_challenge = "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0"


    /**
     * # 创建一个Pixiv账号验证器
     * 该验证器会返回一个Pixiv验证器，请参考[PixivVerification]
     */
    fun newAccount(): PixivVerification = PixivVerification(code_challenge, code_verify)

    /**
     * # 直接创建一个Pixiv账号
     * 该验证器**不会**验证您的数据准确性
     */
    fun newAccountFromConfig(block: PixivAccountConfig.() -> Unit = {}): PixivAccount {
        val config = PixivAccountConfig().apply(block)
        return PixivAccount(config)
    }
}

/**
 * # Pixiv验证器
 *
 */
class PixivVerification(
    private val code_challenge: String,
    private val code_verify: String,
) {
    /**
     * # 获取Pixiv验证url
     * 验证的url需要在浏览器打开
     * @return 验证url
     */
    fun url(): String =
        "web/v1/login?code_challenge=$code_challenge&code_challenge_method=S256&client=pixiv-android"

    /**
     * # 提交验证
     * @param url 验证url
     * @param block Pixiv账号设置
     * @return 成功登录的Pixiv账号
     */
    suspend fun verify(url: String, block: PixivAccountConfig.() -> Unit = {}): PixivAccount {
        if (!url.startsWith("pixiv://account/login?code=")) throw IllegalArgumentException("url is not pixiv://account/login?code=...")

        val code = Url(url).parameters["code"]
        checkNotNull(code)

        HttpClient {
            install(ContentNegotiation) {
                json(top.kagg886.pixko.internal.json)
            }
        }.use { client ->
            val resp = client.post("https://oauth.secure.pixiv.net/auth/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    FormDataContent(
                        Parameters.build {
                            append("client_id", pixiv_client_id)
                            append("client_secret", pixiv_client_secret)
                            append("grant_type", "authorization_code")
                            append("code", code)
                            append("code_verifier", code_verify)
                            append("redirect_uri", "https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback")
                            append("include_policy", "true")
                        }
                    )
                )
            }

            if (resp.status != HttpStatusCode.OK) {
                throw IllegalArgumentException(resp.bodyAsText())
            }

            val json = resp.body<JsonElement>().jsonObject

            //{
            //    "access_token": "1",
            //    "expires_in": 3600,
            //    "token_type": "bearer",
            //    "scope": "",
            //    "refresh_token": "2"
            //}

            val accessToken =
                json["access_token"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("access_token is null")
            val refreshToken =
                json["refresh_token"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("refresh_token is null")

            val config = PixivAccountConfig().apply(block)
            config.storage.setToken(TokenType.ACCESS, accessToken)
            config.storage.setToken(TokenType.REFRESH, refreshToken)

            return PixivAccount(config)
        }
    }
}