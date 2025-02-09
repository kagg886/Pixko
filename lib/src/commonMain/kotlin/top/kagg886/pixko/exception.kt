package top.kagg886.pixko

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.internal.json

data class PixivException(
    val url: String,
    val body: String,
    override val cause: Throwable? = null
) : Exception() {
    val serverMessage by lazy {
        kotlin.runCatching {
            json.decodeFromString<JsonElement>(body)
                .jsonObject["error"]!!
                .jsonObject["user_message"]!!
                .jsonPrimitive
                .content
        }.getOrNull()
    }
}

class InvaidRefreshTokenException : Exception()