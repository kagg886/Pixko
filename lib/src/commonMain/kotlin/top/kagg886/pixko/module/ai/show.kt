package top.kagg886.pixko.module.ai

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount


@Serializable
private data class AiShowSettings(@SerialName("show_ai") val data: Boolean)

/**
 * # 是否允许展示AI内容
 * @return [Boolean] 结果
 */
suspend fun PixivAccount.isAllowedAiShow(): Boolean {
    return client.get("v1/user/ai-show-settings").body<AiShowSettings>().data
}


/**
 * # 设置是否展示AI
 * @param boolean true为展示，false为不展示
 * @throws IllegalStateException 当响应结果与请求结果不一致时抛出异常
 */
suspend fun PixivAccount.setAllowedAiShow(boolean: Boolean) {
    val resp = client.post("v1/user/ai-show-settings/edit") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(FormDataContent(
            Parameters.build {
                append("show_ai", boolean.toString())
            }
        ))
    }.body<AiShowSettings>().data

    check(resp == boolean)
}
