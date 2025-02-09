package top.kagg886.pixko


import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.SHA256
import dev.whyoleg.cryptography.algorithms.SHA512
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.core.*
import kotlinx.datetime.Clock
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.PixivAccountFactory.newAccount
import top.kagg886.pixko.PixivAccountFactory.newAccountFromConfig
import top.kagg886.pixko.internal.json
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

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
    private const val CODES = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"
    private val RANDOM = Random(Clock.System.now().toEpochMilliseconds())
    private val HASH = CryptographyProvider.Default
        .get(SHA256)
        .hasher()

    /**
     * # 创建一个Pixiv账号验证器
     * 该验证器会返回一个Pixiv验证器，请参考[PixivVerification]
     */
    fun newAccount(): PixivVerification {
        val verify = List(128) { CODES.random(RANDOM) }.joinToString("")
        return newAccount(verify)
    }

    /**
     * # 创建一个Pixiv账号验证器
     * 该验证器会返回一个Pixiv验证器，请参考[PixivVerification]
     *
     * > 注意，verify请自行生成。
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun newAccount(verify: String): PixivVerification {
        //final codeChallenge = base64Url
        //        .encode(sha256.convert(ascii.encode(Constants.code_verifier!)).bytes)
        //        .replaceAll('=', '');
        val challenge = Base64.UrlSafe.encode(
            HASH.hashBlocking(
                verify.encodeToByteArray()
            )
        ).replace("=","")
        return PixivVerification(challenge, verify)
    }

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
    internal val codeChallenge: String,
    internal val codeVerify: String,
) {
    /**
     * # 获取Pixiv验证url
     * 验证的url需要在浏览器打开
     * @return 验证url
     */
    @Deprecated("please use the url property)")
    fun url(): String =
        "web/v1/login?code_challenge=$codeChallenge&code_challenge_method=S256&client=pixiv-android"

    /**
     * # 获取Pixiv验证url
     * 验证的url需要在浏览器打开
     * @return 验证url
     */
    val url: String =
        "https://app-api.pixiv.net/web/v1/login?code_challenge=$codeChallenge&code_challenge_method=S256&client=pixiv-android"

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

        val config = PixivAccountConfig().apply(block)

        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(json)
            }
            engine {
                config.engine(this)
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
                            append("code_verifier", codeVerify)
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

            config.storage.setToken(TokenType.ACCESS, accessToken)
            config.storage.setToken(TokenType.REFRESH, refreshToken)

            return PixivAccount(config)
        }
    }
}