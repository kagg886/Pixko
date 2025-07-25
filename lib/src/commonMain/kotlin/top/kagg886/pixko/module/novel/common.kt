package top.kagg886.pixko.module.novel

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.Tag
import top.kagg886.pixko.User
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
data class NovelResult(
    @SerialName("next_url")
    val nextUrl: String? = null,
    val novels: List<Novel>
)

/**
 * # 代表了小说
 * @property id 小说id
 * @property title 小说标题
 * @property caption 小说简介
 * @property imageUrls 小说封面
 * @property createDate 小说创建时间
 * @property tags 小说标签
 * @property user 小说作者
 * @property isBookmarked 是否被收藏
 * @property totalBookmarks 小说收藏数
 * @property totalView 小说浏览数
 * @property totalComments 小说评论数
 * @property novelAiType 小说ai-tag，建议使用[isAI]
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class Novel(
    val id: Int,
    val title: String,
    val caption: String,

    @SerialName("image_urls")
    val imageUrls: ImageUrls,

    @SerialName("create_date")
    @Contextual
    val createDate: Instant,
    val tags: List<Tag>,
//    @SerialName("page_count")
//    val pageCount: Int,
    @SerialName("text_length")
    val textLength: Int,
    val user: User,
    val series: SimpleSeries,
    @SerialName("is_bookmarked")
    val isBookmarked: Boolean,
    @SerialName("total_bookmarks")
    val totalBookmarks: Int,
    @SerialName("total_view")
    val totalView: Int,

    @SerialName("total_comments")
    val totalComments: Int,

    @SerialName("novel_ai_type")
    internal val novelAiType: Int,

    @SerialName("x_restrict")
    internal val xRestrictLevel: Int,
) {
    /**
     * 判断小说是否为ai创作
     */
    val isAI = novelAiType == 2
    val isR18 = xRestrictLevel == 1
    val isR18G = xRestrictLevel == 2
}

/**
 * # 代表了小说系列
 * @property id 系列id
 * @property title 系列标题
 */
@Serializable
data class SimpleSeries(
    val id: Int? = -1,
    val title: String = "",
) {
    /**
     * 判断系列是否有效
     * 无效的系列id默认置为-1
     */
    val isNull = id == -1
}
