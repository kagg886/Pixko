package top.kagg886.pixko.internal

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.*

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
                val subject = subject.call.save().response
                proceedWith(subject)
                val originBody = kotlin.runCatching {
                    subject.bodyAsText()
                }.getOrElse { "" }

                if (subject.status == HttpStatusCode.BadRequest) {
                    if (originBody.contains("Invalid access token") || originBody.contains("Error occurred at the OAuth process")) {
                        val body = kotlin.runCatching {
                            scope.post("https://oauth.secure.pixiv.net/auth/token") {
                                contentType(ContentType.Application.FormUrlEncoded)
                                setBody(
                                    FormDataContent(
                                        Parameters.build {
                                            append("client_id", pixiv_client_id)
                                            append("client_secret", pixiv_client_secret)
                                            append("grant_type", "refresh_token")
                                            append("refresh_token", storage.getToken(TokenType.REFRESH)!!)
                                            append("include_policy", "true")
                                        }
                                    )
                                )
                            }
                            //对外隐藏auth/token请求。一般来说这个阶段抛出错误导致REFRESH_TOKEN丢失，需要重新登录。
                        }.getOrNull() ?: throw PixivException(subject.call.request.url.toString(), originBody)

                        val resp = body.body<JsonElement>().jsonObject

                        val accessToken =
                            resp["access_token"]?.jsonPrimitive?.content
                                ?: throw IllegalArgumentException("access_token is null")
                        storage.setToken(TokenType.ACCESS, accessToken)

                        val realResp =
                            scope.request(HttpRequestBuilder().takeFrom(subject.request)).call.save().response
                        proceedWith(realResp)
                        return@intercept
                    }
                }
                if (subject.status != HttpStatusCode.OK) {
                    throw PixivException(subject.call.request.url.toString(), originBody)
                }
            }
        }
    }
}
