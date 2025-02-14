package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json

/**
 * # 获取插画详情
 * @param illustId 插画id
 * @return [Illust]
 *
 */
suspend fun PixivAccount.getIllustDetail(illustId: Long): Illust {
    return client.get("v1/illust/detail?illust_id=$illustId")
        .body<JsonElement>().jsonObject["illust"]!!.jsonObject.let {
        json.decodeFromJsonElement(it)
    }
}
