package top.kagg886.pixko.module.illust

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
 * @property illustAiType AI-tag标记，不建议使用。建议使用[isAI]
 * @property singlePageMeta 单页原画数据，不建议使用。建议使用[singlePageMeta]
 */
@Serializable
data class Illust(
    val id: Int,
    val title: String,
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

    @SerialName("image_urls")
    val imageUrls: ImageUrls,
    @SerialName("meta_pages")
    internal val _metaPages: List<ImageUrlsWrapper>, //多页漫画详情接口包含origin
    @SerialName("meta_single_page")
    internal val singlePageMeta: JsonElement? = null, //单页漫画详情接口包含origin
) {
    /**
     * 是否为R18
     */
    val isR18: Boolean = xRestrict == 1 || sanityLevel >= 4

    /**
     * 是否为AI
     */
    val isAI: Boolean = illustAiType == 2

    @Deprecated("please use contentImages")
    val metaPages by lazy {
        _metaPages.map { it.imageUrls }
    }

    val contentImages: List<String>? by lazy {
        if (pageCount > 1) {
            return@lazy _metaPages.map { it.imageUrls.content }
        }
        return@lazy listOf(imageUrls.content)
    }

    /**
     * 获取原图，当信息未包含原图时返回null
     */
    val originImages: List<String>? by lazy {
        if (pageCount > 1) {
            return@lazy _metaPages.mapNotNull { it.imageUrls.original }.toList().ifEmpty { null }
        }
        singlePageMeta?.let {
            listOf(it.jsonObject["original_image_url"]!!.jsonPrimitive.content)
        }
    }

    val limitLevel: LimitLevel by lazy {
        when (imageUrls.content) {
            "https://s.pximg.net/common/images/limit_r18_360.png" -> LimitLevel.LIMIT_R18
            "https://s.pximg.net/common/images/limit_sanity_level_360.png" -> LimitLevel.LIMIT_R15
            "https://s.pximg.net/common/images/limit_unknown_360.png" -> LimitLevel.LIMIT_PRIVACY
            else -> LimitLevel.NONE
        }
    }

    val isLimited: Boolean by lazy {
        limitLevel != LimitLevel.NONE
    }

    enum class LimitLevel {
        NONE,
        LIMIT_R15,
        LIMIT_R18,
        LIMIT_PRIVACY
    }
}