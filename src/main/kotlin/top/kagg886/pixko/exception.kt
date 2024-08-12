package top.kagg886.pixko

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.internal.json
import java.io.IOException

data class PixivException(
    val url: String,
    val body: String,
    override val cause: Throwable? = null
) : IOException() {
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