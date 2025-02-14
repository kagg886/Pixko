package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount

/**
 * # 小说系列详情
 * @property id 小说系列id
 * @property title 小说系列标题
 * @property caption 小说系列描述
 * @property original 是否原创
 * @property concluded 是否处于连载状态
 * @property count 小说数量
 */
@Serializable
data class SeriesDetail(
    val id: Long,
    val title: String,
    @SerialName("display_text")
    val caption: String,

    @SerialName("is_original")
    val original: Boolean,
    @SerialName("is_concluded")
    val concluded: Boolean,

    @SerialName("count")
    val count: Int,
)

/**
 * # 小说系列列表
 * @property data 小说系列列表
 * @property next 下一页url
 * > TODO 暂时不知道next_url的控制参数是offset还是其他的，如若知晓则此类可能会被弃用
 */
@Serializable
data class SeriesResult(
    @SerialName("novel_series_details")
    val data: List<SeriesDetail>,
    @SerialName("next_url")
    val next: String?
)


/**
 * # 获取用户的小说系列列表
 * @param userId 用户id
 * @return 小说系列列表
 */
suspend fun PixivAccount.getNovelSeries(userId: Long): SeriesResult {
    return client.get("v1/user/novel-series") {
        parameter("user_id", userId)
    }.body()
}
