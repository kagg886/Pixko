package top.kagg886.pixko

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
        return name
    }

    override fun hashCode(): Int {
        return name.hashCode()
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