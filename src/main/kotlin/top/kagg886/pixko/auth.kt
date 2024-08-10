package top.kagg886.pixko

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive


internal const val pixiv_client_id = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
internal const val pixiv_client_secret = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"

object PixivAccountFactory {

    internal const val code_verify = "-29P7XEuFCNdG-1aiYZ9tTeYrABWRHxS9ZVNr6yrdcI"
    internal const val code_challenge = "usItTkssolVsmIbxrf0o-O_FsdvZFANVPCf9jP4jP_0"


    fun newAccount(): PixivVerification = PixivVerification(code_challenge, code_verify)

    fun newAccountFromConfig(block: PixivAccountConfig.() -> Unit = {}): PixivAccount {
        val config = PixivAccountConfig().apply(block)
        return PixivAccount(config)
    }
}


class PixivVerification(
    private val code_challenge: String,
    private val code_verify: String,
) {
    fun url(): String =
        "https://app-api.pixiv.net/web/v1/login?code_challenge=$code_challenge&code_challenge_method=S256&client=pixiv-android"

    //pixiv://account/login?code=BsQND5vc6uIWKIwLiDsh0S3h1yno6eVHDVMrX3fONgM&via=login
    suspend fun verify(url: String, block: PixivAccountConfig.() -> Unit = {}): PixivAccount {
        if (!url.startsWith("pixiv://account/login?code=")) throw IllegalArgumentException("url is not pixiv://account/login?code=...")

        val code = Url(url).parameters["code"]

        HttpClient().use { client ->
            val resp = client.post("https://oauth.secure.pixiv.net/auth/token") {
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(
                    listOf(
                        "client_id" to pixiv_client_id,
                        "client_secret" to pixiv_client_secret,
                        "grant_type" to "authorization_code",
                        "code" to code,
                        "code_verifier" to code_verify,
                        "redirect_uri" to "https://app-api.pixiv.net/web/v1/users/auth/pixiv/callback",
                        "include_policy" to true,
                    ).joinToString("&") { (k, v) -> "$k=$v" }
                )
            }.body<JsonElement>().jsonObject

            //{
            //    "access_token": "1",
            //    "expires_in": 3600,
            //    "token_type": "bearer",
            //    "scope": "",
            //    "refresh_token": "2"
            //}

            val accessToken =
                resp["access_token"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("access_token is null")
            val refreshToken =
                resp["refresh_token"]?.jsonPrimitive?.content ?: throw IllegalArgumentException("refresh_token is null")

            val config = PixivAccountConfig().apply(block)
            config.storage.setToken(TokenType.ACCESS, accessToken)
            config.storage.setToken(TokenType.REFRESH, refreshToken)

            return PixivAccount(config)
        }
    }
}