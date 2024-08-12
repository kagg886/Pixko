package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.utils.io.core.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json
import top.kagg886.pixko.module.profile.CountryCode
import top.kagg886.pixko.module.profile.JapanAddress
import top.kagg886.pixko.module.profile.Job
import java.io.InputStream

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

/**
 * # 获取当前登录用户的一些信息
 * @return [SimpleMeProfile] 简介
 *
 */
suspend fun PixivAccount.getCurrentUserSimpleProfile(): SimpleMeProfile {
    return json.decodeFromJsonElement(client.get("v1/user/me/state").body<JsonElement>().jsonObject["profile"]!!)
}

class UserProfileSetting {
    lateinit var gender: Gender
    lateinit var address: JapanAddress
    lateinit var country: CountryCode
    lateinit var job: Job
    lateinit var userName: String
    lateinit var birthday: LocalDate


    var webPage: String = ""
    var twitter: String = ""
    var comment: String = ""
    var avatar: InputStream? = null
}

/**
 * # 构建用户设置(除头像)
 * @param block 构建DSL
 * @return [UserProfileSetting] 用户设置
 */
fun UserInfo.buildSettings(block: UserProfileSetting.() -> Unit): UserProfileSetting {
    val user = this.user
    val setting = this.profile
    val k = UserProfileSetting().apply {
        gender = setting.gender
        address = setting.address
        country = setting.country
        job = setting.job
        userName = user.name
        birthday = setting.birth ?: LocalDate.fromEpochDays(0)
        webPage = setting.webpage ?: ""
        twitter = setting.twitterUrl ?: ""
        comment = user.comment ?: ""
    }.apply(block)


    check((k.address == JapanAddress.INTERNATIONAL) xor (k.country == CountryCode.JAPAN)) {
        "address and country 不符合规矩"
    }
    return k
}

suspend fun PixivAccount.setUserProfile(profile: UserProfileSetting) {
    client.post("v1/user/profile/edit") {
        contentType(ContentType.MultiPart.FormData)
        setBody(MultiPartFormDataContent(formData {
            append("gender", profile.gender.name.lowercase())
            append("address", profile.address.code)
            if (profile.address == JapanAddress.INTERNATIONAL) {
                append("country", profile.country.code)
            }
            append("job", profile.job.code)
            append("user_name", profile.userName)
            append("webpage", profile.webPage)
            append("twitter", profile.twitter)
            append("comment", profile.comment)
            append("birthday", profile.birthday.toString())
            profile.avatar?.let {
                append("profile_image", "profile.jpeg", ContentType.Image.JPEG) {
                    this.writePacket(ByteReadPacket(it.readAllBytes()))
                }
            }
        }))
    }
}