package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDateTime
import top.kagg886.pixko.PixivAccount

enum class SearchSort {
    DESC,
    ASC
}

enum class SearchTarget {
    EXACT_MATCH_FOR_TAGS,
    PARTIAL_MATCH_FOR_TAGS,
    TITLE_AND_CAPTION
}

data class SearchConfig(
    var sort: SearchSort = SearchSort.DESC,
    var searchTarget: SearchTarget = SearchTarget.PARTIAL_MATCH_FOR_TAGS,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var page: Int = 1
)

suspend fun PixivAccount.searchIllust(
    word: String,
    block: SearchConfig.() -> Unit = {}
): IllustResult {
    val (sort, searchTarget, startDate, endDate, page) = SearchConfig().apply(block)
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