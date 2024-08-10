package top.kagg886.pixko.module.illust

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IllustResult(
    @SerialName("next_url")
    val nextUrl: String,
    val illusts: List<Illust>
)
@Serializable
data class Illust(
    val id: Int,
    val title: String,
    @SerialName("image_urls")
    val imageUrls: ImageUrls,
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
    val isR18: Boolean = xRestrict == 1 || sanityLevel >= 4
    val isAi: Boolean = illustAiType == 2
}

@Serializable
data class Tag(
    val name: String,
    @SerialName("translated_name")
    val translatedName: String?
)

@Serializable
data class ImageUrls(
    val square_medium: String,
    val medium: String,
    val large: String
)

@Serializable
data class User(
    val id: Int,
    val name: String,
    val account: String,
    @SerialName("profile_image_urls")
    val profileImageUrls: ProfileImage,

    @SerialName("is_followed")
    val isFollowed: Boolean
)

@Serializable
data class ProfileImage(
    val medium: String
)