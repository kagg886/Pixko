package top.kagg886.pixko.module.illust

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.Tag
import top.kagg886.pixko.User
import top.kagg886.pixko.module.novel.Novel

/**
 * # 插画包装列表
 * @property nextUrl 下一页的url
 * @property illusts 插画列表
 */
@Serializable
data class IllustResult(
    @SerialName("next_url")
    val nextUrl: String? = null,
    val illusts: List<Illust>
)

/**
 * # 小说包装列表
 * @property nextUrl 下一页的url
 * @property novels 小说列表
 */
@Serializable
data class NovelResult(
    @SerialName("next_url")
    val nextUrl: String?,
    val novels: List<Novel>
)

@Serializable
data class ImageUrlsWrapper(
    @SerialName("image_urls")
    val imageUrls: ImageUrls,
)

/**
 * # 代表了一个插画
 *
 * @property id 插画id
 * @property title 插画标题
 * @property imageUrls 插画图片链接
 * @property caption 插画描述,为html格式
 * @property user 插画作者
 * @property tags 插画标签
 * @property createTime 插画创建时间
 * @property pageCount 插画页数
 * @property width 插画宽度
 * @property height 插画高度
 * @property sanityLevel 插画Sanity等级，不建议使用。建议使用[isR18]
 * @property _metaPages 多页插画的每页地址，不建议使用。建议使用[metaPages]
 * @property xRestrict 未知，与r18有关
 * @property totalView 插画总浏览量
 * @property totalBookmarks 插画总收藏数
 * @property isBookMarked 是否收藏
 * @property illustAiType AI-tag标记，不建议使用。建议使用[isAi]
 */
@Serializable
data class Illust(
    val id: Int,
    val title: String,
    @SerialName("image_urls")
    val imageUrls: ImageUrls,
    @SerialName("meta_pages")
    internal val _metaPages:List<ImageUrlsWrapper>,
    //html encoded
    val caption: String,

    val user: User,
    val tags: List<Tag>,
    @SerialName("create_date")
    val createTime: Instant,

    @SerialName("page_count")
    val pageCount: Int,
    val width: Int,
    val height: Int,

    @SerialName("sanity_level")
    internal val sanityLevel: Int,

    @SerialName("x_restrict")
    internal val xRestrict: Int,

    @SerialName("total_view")
    val totalView: Int,
    @SerialName("total_bookmarks")
    val totalBookmarks: Int,
    @SerialName("is_bookmarked")
    val isBookMarked: Boolean,

    @SerialName("illust_ai_type")
    internal val illustAiType: Int,
) {
    /**
     * 是否为R18
     */
    val isR18: Boolean = xRestrict == 1 || sanityLevel >= 4

    /**
     * 是否为AI
     */
    val isAI: Boolean = illustAiType == 2

    val metaPages by lazy {
        _metaPages.map { it.imageUrls }
    }
}

