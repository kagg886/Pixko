package top.kagg886.pixko.module.novel

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json

/**
 * # 获取小说详情
 * @param novelId 小说id
 * @return [Novel]
 *
 */
suspend fun PixivAccount.getNovelDetail(novelId: Long): Novel {
    return client.get("v2/novel/detail?novel_id=$novelId")
        .body<JsonElement>().jsonObject["novel"]!!.jsonObject.let {
        json.decodeFromJsonElement(it)
    }
}