package top.kagg886.pixko.module.search

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json
import top.kagg886.pixko.module.illust.User

/**
 * # 搜索用户
 * @param keyword 关键词
 * @param page 页码
 * @return [List] 用户列表
 */
suspend fun PixivAccount.searchUser(
    keyword: String,
    page: Int = 1,
): List<User> {
    return client.get("v1/search/user") {
        parameter("word", keyword)
        parameter("page", (page - 1) * 30)
    }.body<JsonElement>().jsonObject["user_previews"]!!.jsonArray.map {
        json.decodeFromJsonElement<User>(it.jsonObject["user"]!!)
    }
}