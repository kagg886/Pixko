package top.kagg886.pixko

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * # 代表一个最简单的用户信息
 *
 * @property id 用户id
 * @property name 用户名
 * @property account 用户账号
 * @property profileImageUrls 用户头像链接
 * @property isFollowed 是否关注了这个用户，某些接口不会返回这个值
 * @property comment 用户个人签名，某些接口不会返回这个值
 */
@Serializable
data class User(
    val id: Int,
    val name: String,
    val account: String,
    @SerialName("profile_image_urls")
    val profileImageUrls: ImageUrls,

    @SerialName("is_followed")
    val isFollowed: Boolean? = null,
    val comment: String? = null,
)

/**
 * # 标签
 * @property name 标签原始名称(与本国语言有关)
 * @property translatedName 标签翻译名称(与当前语言有关)
 */
@Serializable
data class Tag(
    val name: String,
    @SerialName("translated_name")
    val translatedName: String? = null
) {
    override fun toString(): String {
        return translatedName ?: name
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Tag

        return name == other.name
    }
}

/**
 * # 图片链接
 * 建议使用[content]变量自适应获取大小
 * @property squareMedium 宽高为 240x240 的图片链接
 * @property medium 宽高为 480x480 的图片链接
 * @property large 宽高为 1200x1200 的图片链接
 * @see content
 */
@Serializable
data class ImageUrls(
    @SerialName("square_medium")
    val squareMedium: String? = null,
    val medium: String? = null,
    val large: String? = null
) {
    val content
        get() = arrayOf(medium, squareMedium, large).firstNotNullOf { it }
}