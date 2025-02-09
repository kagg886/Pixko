package top.kagg886.pixko.internal

import io.ktor.client.call.*
import io.ktor.client.plugins.api.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.*
import top.kagg886.pixko.pixiv_client_id

@KtorDsl
class TokenAutoRefreshPluginV2Config {
    lateinit var storage: TokenStorage
}


@OptIn(InternalAPI::class)
val TokenAutoRefreshPluginV2 = createClientPlugin("TokenAutoRefreshPluginV2", ::TokenAutoRefreshPluginV2Config) {
    val storage = pluginConfig.storage


    onRequest { request, _ ->
        request.headers["Authorization"] = "Bearer ${storage.getToken(TokenType.ACCESS)}"
    }


    on(Send) { originalRequest ->
        val origin = proceed(originalRequest)
        if (origin.response.status == HttpStatusCode.OK) return@on origin

        val body = origin.response.bodyAsBytes()

        with(body.decodeToString()) {
            if (!(contains("Invalid access token") || contains("Error occurred at the OAuth process"))) {
                if (this.contains("Invalid refresh token")) {
                    throw InvaidRefreshTokenException()
                }
                throw PixivException(origin.request.url.toString(), this)
            }
        }

        val resp = client.post("https://oauth.secure.pixiv.net/auth/token") {
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

        val json = resp.body<JsonElement>().jsonObject

        val accessToken = json["access_token"]?.jsonPrimitive?.content
            ?: throw InvaidRefreshTokenException()

        storage.setToken(TokenType.ACCESS, accessToken)

        val newRequest = HttpRequestBuilder().apply {
            takeFromWithExecutionContext(originalRequest)
            headers["Authorization"] = "Bearer $accessToken"
        }
        return@on proceed(newRequest);
    }
}