package top.kagg886.pixko.module.illust

import top.kagg886.pixko.PixivAccount
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDateTime

enum class SearchSort {
    DESC,
    ASC
}

enum class SearchTarget {
    EXACT_MATCH_FOR_TAGS,
    PARTIAL_MATCH_FOR_TAGS,
    TITLE_AND_CAPTION
}

suspend fun PixivAccount.searchIllust(
    word: String,
    sort: SearchSort = top.kagg886.pixko.module.illust.SearchSort.DESC,
    searchTarget: SearchTarget = top.kagg886.pixko.module.illust.SearchTarget.PARTIAL_MATCH_FOR_TAGS,
    startDate: LocalDateTime? = null,
    endDate: LocalDateTime? = null,
    page: Int = 1
): IllustResult {
    val body = client.get("v1/search/illust") {
        parameter("filter", "for_android")
        parameter("include_translated_tag_results", true)
        parameter("word", word)
        parameter("sort", "date_${sort.name.lowercase()}")
        parameter("search_target", searchTarget.name.lowercase())
        if (startDate != null) {
            parameter("start_date", startDate.toString())
        }
        if (endDate != null) {
            parameter("end_date", endDate.toString())
        }
        parameter("offset", (page - 1) * 30)
    }
    return body.body()
}