package top.kagg886.pixko.internal

import top.kagg886.pixko.TokenStorage
import top.kagg886.pixko.TokenType
import top.kagg886.pixko.pixiv_client_id
import top.kagg886.pixko.pixiv_client_secret
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import kotlinx.coroutines.cancel
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.io.use

internal class TokenAutoRefreshPlugin(
    val tokenStorage: TokenStorage
) {
    class Config {
        lateinit var tokenStorage: TokenStorage
    }

    companion object : HttpClientPlugin<Config, TokenAutoRefreshPlugin> {
        override val key: AttributeKey<TokenAutoRefreshPlugin>
            get() = AttributeKey(TokenAutoRefreshPlugin::class.simpleName!!)

        override fun prepare(block: Config.() -> Unit): TokenAutoRefreshPlugin =
            TokenAutoRefreshPlugin(Config().apply(block).tokenStorage)

        override fun install(plugin: TokenAutoRefreshPlugin, scope: HttpClient) {
            val storage = plugin.tokenStorage
            scope.requestPipeline.intercept(HttpRequestPipeline.Before) {
                context.headers["Authorization"] = "Bearer ${storage.getToken(TokenType.ACCESS)}"
            }
            scope.receivePipeline.intercept(HttpReceivePipeline.After) {
                if (subject.status == HttpStatusCode.BadRequest) {
                    val originBody = subject.bodyAsText()

                    if (originBody.contains("Invalid access token") || originBody.contains("Error occurred at the OAuth process")) {
                        HttpClient(CIO) {
                            install(ContentNegotiation) {
                                json(json)
                            }
                        }.use { client ->
                            val resp = client.post("https://oauth.secure.pixiv.net/auth/token") {
                                contentType(ContentType.Application.FormUrlEncoded)
                                setBody(
                                    listOf(
                                        "client_id" to pixiv_client_id,
                                        "client_secret" to pixiv_client_secret,
                                        "grant_type" to "refresh_token",
                                        "refresh_token" to storage.getToken(TokenType.REFRESH),
                                        "include_policy" to true
                                    ).joinToString("&") { (k, v) -> "$k=$v" }
                                )
                            }.body<JsonElement>().jsonObject

                            val accessToken =
                                resp["access_token"]?.jsonPrimitive?.content
                                    ?: throw IllegalArgumentException("access_token is null")
                            storage.setToken(TokenType.ACCESS, accessToken)

                            val realResp = scope.request(HttpRequestBuilder().takeFrom(subject.request))
//                            proceedWith(HttpResponseContainer(typeInfo, realResp.bodyAsChannel()))
                            proceedWith(realResp)
                        }
                        return@intercept
                    }
                    cancel(json.decodeFromString<JsonElement>(originBody).jsonObject["error"]!!.jsonObject.toString())
                }
            }
        }
    }
}