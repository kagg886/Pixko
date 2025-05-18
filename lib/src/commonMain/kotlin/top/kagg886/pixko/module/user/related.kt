package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.User
import top.kagg886.pixko.internal.json

/**
 * 获取用户相关的用户
 * @param userId 用户ID
 * @return 相关用户列表
 */
suspend fun PixivAccount.getRelatedUser(userId: Long): RelatedUserResult {
    val obj = client.get("v1/user/related") {
        parameter("seed_user_id", userId)
        parameter("filter", "for_android")
    }.body<JsonElement>()
    val nextUrl = obj.jsonObject["next_url"]?.jsonPrimitive?.content
    val data = obj.jsonObject["user_previews"]!!.jsonArray.map {
        json.decodeFromJsonElement<User>(it.jsonObject["user"]!!)
    }
    return RelatedUserResult(nextUrl, data)
}

/**
 * 获取下一页相关用户
 * @param result 上一页的结果
 * @return 下一页的结果
 */
suspend fun PixivAccount.getRelatedUserNext(result: RelatedUserResult): RelatedUserResult? {
    return if (result.next_url == null) null else client.get(result.next_url).body<JsonElement>().run {
        val nextUrl = jsonObject["next_url"]?.jsonPrimitive?.content
        val data = jsonObject["user_previews"]!!.jsonArray.map {
            json.decodeFromJsonElement<User>(it.jsonObject["user"]!!)
        }
        RelatedUserResult(nextUrl, data)
    }
}

@Serializable
data class RelatedUserResult(
    val next_url: String? = null,
    val user_previews: List<User> = emptyList()
)
