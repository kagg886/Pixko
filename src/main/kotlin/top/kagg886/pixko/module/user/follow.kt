package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.User
import top.kagg886.pixko.internal.json
import top.kagg886.pixko.module.illust.Comment
import top.kagg886.pixko.module.illust.IllustFollowPublicity

/**
 * # 关注一个用户
 * @param userId 用户id
 * @param publicity 关注类型
 */
suspend fun PixivAccount.followUser(userId: Int, publicity: UserLikePublicity = UserLikePublicity.PUBLIC) {
    client.post("v1/user/follow/add") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    append("user_id", userId.toString())
                    append("restrict", publicity.name.lowercase())
                }
            )
        )
    }
}

/**
 * # 取消关注一个用户
 * @param userId 用户id
 */
suspend fun PixivAccount.unFollowUser(userId: Int) {
    client.post("v1/user/follow/delete") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    append("user_id", userId.toString())
                }
            )
        )
    }
}

/**
 * # 用户关注列表的设置
 * @property page 页码
 * @property publicity 关注类型(私有关注类型仅对自己的用户id生效)
 */
class FollowListSetting {
    var page = 1
    var publicity = UserLikePublicity.PUBLIC
}

/**
 * # 获取用户的关注列表
 * @param userId 用户id
 * @param options 设置
 * @return [List] 关注列表
 * @see FollowListSetting
 * @see UserInfo
 */
suspend fun PixivAccount.getFollowingList(
    userId: Int,
    options: FollowListSetting.() -> Unit = {}
): List<User> {
    val config = FollowListSetting().apply(options)
    val body = client.get("v1/user/following?filter=for_android") {
        parameter("user_id", userId)
        parameter("offset", (config.page - 1) * 30)
        parameter("restrict", config.publicity.name.lowercase())
    }.body<JsonElement>()
    return body
        .jsonObject["user_previews"]!!
        .jsonArray
        .map {
            json.decodeFromJsonElement(it.jsonObject["user"]!!)
        }
}
