package top.kagg886.pixko.module.novel

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.Tag
import top.kagg886.pixko.User

@Serializable
data class NovelResult(
    @SerialName("next_url")
    val nextUrl:String? = null,
    val novels:List<Novel>
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
@Serializable
data class Novel(
    val id: Int,
    val title: String,
    val caption: String,

    @SerialName("image_urls")
    val imageUrls: ImageUrls,

    @SerialName("create_date")
    val createDate: Instant,
    val tags: List<Tag>,
//    @SerialName("page_count")
//    val pageCount: Int,
    @SerialName("text_length")
    val textLength: Int,
    val user: User,

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
) {
    /**
     * 判断小说是否为ai创作
     */
    val isAI = novelAiType == 2
}