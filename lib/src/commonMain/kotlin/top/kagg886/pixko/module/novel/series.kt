package top.kagg886.pixko.module.novel

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.User

/**
 * # 小说系列
 * @param novelSeriesDetail 系列的摘要
 * @param novels 系列的作品列表
 */
@Serializable
data class SeriesInfo(
    @SerialName("novel_series_detail")
    val novelSeriesDetail: SeriesDetail,

    @SerialName("novels")
    val novels: List<Novel>,
)

@Serializable
data class SeriesDetail(
    val id: Int,
    val title: String,
    val caption: String,

    @SerialName("content_count") val coutentCount: Int,
    @SerialName("total_character_count") val totalCharacterCount: Int,
    val user: User,
) {
    val pageCount by lazy {
        with(coutentCount) {
            this / 30 + this % 30
        }
    }
}

/**
 * # 获取小说系列列表
 * @param series_id 系列ID
 * @return 小说系列列表
 */
suspend fun PixivAccount.getNovelSeries(id: Int, page: Int = 1): SeriesInfo {
    return client.get("v2/novel/series") {
        parameter("series_id", id)
        parameter("last_order", (page - 1) * 30)
    }.body<SeriesInfo>()
}
