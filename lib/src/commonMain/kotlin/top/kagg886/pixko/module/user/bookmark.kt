package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.Tag
import top.kagg886.pixko.module.illust.IllustResult
import top.kagg886.pixko.module.illust.NovelResult
import top.kagg886.pixko.module.user.FavoriteTagsType.Illust
import top.kagg886.pixko.module.user.FavoriteTagsType.Novel
import top.kagg886.pixko.module.user.TagFilter.*
import top.kagg886.pixko.module.user.UserLikePublicity.PRIVATE
import top.kagg886.pixko.module.user.UserLikePublicity.PUBLIC

/**
 * # 用户收藏类别
 * @property PUBLIC 公开
 * @property PRIVATE 私密
 */
enum class UserLikePublicity {
    PUBLIC,
    PRIVATE,
}

/**
 * # 收藏的所有标签
 * @property Illust 收藏的插画
 * @property Novel 收藏的小说
 */
enum class FavoriteTagsType {
    Illust, Novel
}

@Serializable
data class FavoriteTags(
    val name: String,
    val count: Int
)

/**
 * # 标签过滤器
 * @property NoFilter 不过滤
 * @property FilterWithoutTagged 只显示未标记的
 * @property FilterWithTag 只显示指定标签的
 */
sealed interface TagFilter {
    sealed interface HasTag {
        val tag: Tag
    }

    data object NoFilter : TagFilter
    data object FilterWithoutTagged : TagFilter, HasTag {
        override val tag = Tag("未分類", "未分类")
    }

    data class FilterWithTag(override val tag: Tag) : TagFilter, HasTag
}

/**
 * # 获取用户的收藏标签
 * @param restrict 公开性
 * @param favoriteTagsType 收藏的标签类型
 * @return [FavoriteTags] 收藏的标签
 */
suspend fun PixivAccount.getAllFavoriteTags(
    restrict: UserLikePublicity = PUBLIC,
    favoriteTagsType: FavoriteTagsType = Illust,
    page: Int = 1
): List<FavoriteTags> {

    @Serializable
    data class FavoriteTagsReturn(
        @SerialName("bookmark_tags")
        val tags: List<FavoriteTags>,
    )

    return client.get("v1/user/bookmark-tags/${favoriteTagsType.name.lowercase()}") {
        parameter("restrict", restrict.name.lowercase())
        parameter("offset", (page - 1) * 30)
    }.body<FavoriteTagsReturn>()
        .tags
}

/**
 * # 获取用户收藏的插画
 * @param userId 用户id
 * @param restrict 公开性
 * @param tags 标签
 * @return [IllustResult] 用户收藏的插画
 *
 * @see UserLikePublicity
 * @see TagFilter
 */
suspend fun PixivAccount.getUserLikeIllust(
    userId: Int,
    restrict: UserLikePublicity = PUBLIC,
    filter: TagFilter = NoFilter
): IllustResult {
    return client.get("v1/user/bookmarks/illust") {
        parameter("user_id", userId)
        parameter("restrict", restrict.name.lowercase())
        when (filter) {
            is HasTag -> parameter("tag", filter.tag.name)
            else -> Unit
        }
    }.body()
}

/**
 * # 获取用户收藏的小说
 * @param userId 用户id
 * @param restrict 公开性
 * @param filter 标签过滤器
 * @return [NovelResult] 用户收藏的小说
 *
 * @see UserLikePublicity
 * @see TagFilter
 */
suspend fun PixivAccount.getUserLikeNovel(
    userId: Int,
    restrict: UserLikePublicity = PUBLIC,
    filter: TagFilter = NoFilter
): NovelResult {
    return client.get("v1/user/bookmarks/novel") {
        parameter("user_id", userId)
        parameter("restrict", restrict.name.lowercase())
        when (filter) {
            is HasTag -> parameter("tag", filter.tag.name)
            else -> Unit
        }
    }.body()
}

/**
 * # 获取下一页用户收藏的插画
 * @param result [IllustResult]
 * @return [IllustResult] 用户收藏的插画，若为null则代表没有下一页
 */
suspend fun PixivAccount.getUserLikeIllustNext(result: IllustResult): IllustResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl).body()
}

/**
 * # 获取下一页用户收藏的小说
 * @param result [NovelResult]
 * @return [NovelResult] 用户收藏的小说，若为null则代表没有下一页
 * @see getUserLikeNovel
 */
suspend fun PixivAccount.getUserLikeNovelNext(result: NovelResult): NovelResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl).body()
}
