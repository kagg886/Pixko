package top.kagg886.pixko.module.trending

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.Tag
import top.kagg886.pixko.module.illust.Illust

/**
 * # 热门标签
 * @property _tag 标签名,推荐使用 [Tag.name]
 * @property translatedName 标签翻译,推荐使用 [Tag.translatedName]
 * @property illust 插画
 * @see Tag
 */
@Serializable
data class TrendingTags(
    @SerialName("tag")
    internal val _tag: String,
    @SerialName("translated_name")
    internal val translatedName: String? = null,
    val illust: Illust
) {
    /**
     * 标签
     */
    val tag: Tag by lazy {
        Tag(_tag, translatedName)
    }
}


/**
 * # 获取热门标签
 * @return [List] 热门标签
 * @see TrendingTags
 */
suspend fun PixivAccount.getRecommendTags(): List<TrendingTags> {
    //(小说的返回似乎和插画一样)
    @Serializable
    data class TrendTagResponse(
        @SerialName("trend_tags")
        val data: List<TrendingTags>
    )
    return client.get("v1/trending-tags/illust") {
        parameter("filter", "for_android")
        parameter("include_translated_tag_results", "true")
    }.body<TrendTagResponse>().data
}