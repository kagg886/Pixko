package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json

/**
 * # 个人简介
 * @property userId 用户id
 * @property pixivId 用户自己设置的pixiv_id
 * @property name 用户名
 * @property profileImageUrls 用户头像
 * @property isPremium 是否为会员
 * @property xRestrict 未知
 */
@Serializable
data class SimpleMeProfile(
    @SerialName("user_id")
    val userId: Int,
    @SerialName("pixiv_id")
    val pixivId: String,
    @SerialName("name")
    val name: String,
    @SerialName("profile_image_urls")
    val profileImageUrls: ImageUrls, // 假设 `img` 是一个任意类型的值，具体类型未知
    @SerialName("is_premium")
    val isPremium: Boolean,
    @SerialName("x_restrict")
    val xRestrict: Int
)

//{
//    "user_state": {
//        "is_mail_authorized": true,
//        "has_mail_address": true,
//        "has_changed_pixiv_id": false,
//        "can_change_pixiv_id": true,
//        "has_password": true,
//        "require_policy_agreement": false,
//        "no_login_method": false
//    },
//    "profile": {
//        "user_id": int,
//        "pixiv_id": "string",
//        "name": "string",
//        "profile_image_urls": img
//        "is_premium": bool,
//        "x_restrict": int
//    }
//}

/**
 * # 获取当前登录用户的一些信息
 * @return [SimpleMeProfile] 简介
 * @sample UserTest.testProfile
 */
suspend fun PixivAccount.getCurrentUserSimpleProfile(): SimpleMeProfile {
    return json.decodeFromJsonElement(client.get("v1/user/me/state").body<JsonElement>().jsonObject["profile"]!!)
}