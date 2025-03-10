package top.kagg886.pixko.module.search

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDateTime
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.Illust
import top.kagg886.pixko.module.illust.IllustResult
import top.kagg886.pixko.module.search.SearchSort.*
import top.kagg886.pixko.module.search.SearchTarget.*
import top.kagg886.pixko.module.user.getCurrentUserSimpleProfile

/**
 * 搜索结果
 * @property DATE_DESC 按时间降序
 * @property DATE_ASC 按时间升序
 * @property POPULAR_DESC 按热度降序
 */
enum class SearchSort {
    DATE_DESC,
    DATE_ASC,
    POPULAR_DESC
}

/**
 * # 搜索目标
 * @property EXACT_MATCH_FOR_TAGS 精确匹配标签
 * @property PARTIAL_MATCH_FOR_TAGS 模糊匹配标签
 * @property TITLE_AND_CAPTION 标题和描述 (仅限插画)
 * @property TEXT 描述(仅限小说)
 * @property KEYWORD 关键词(仅限小说)
 *
 */
enum class SearchTarget {
    EXACT_MATCH_FOR_TAGS,
    PARTIAL_MATCH_FOR_TAGS,
    TITLE_AND_CAPTION,
    TEXT,
    KEYWORD
}

/**
 * # 搜索配置
 * @property sort 排序方式
 * @property searchTarget 搜索目标
 * @property startDate 开始日期
 * @property endDate 结束日期
 * @property page 页码
 */
data class SearchConfig(
    var sort: SearchSort = DATE_DESC,
    var searchTarget: SearchTarget = PARTIAL_MATCH_FOR_TAGS,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var page: Int = 1
)

/**
 * # 搜索图片
 * @param word 搜索关键词
 * @param block 搜索配置
 * @return [IllustResult] 搜索结果
 */
suspend fun PixivAccount.searchIllust(
    word: String,
    block: SearchConfig.() -> Unit = {}
): List<Illust> {
    val (sort, searchTarget, startDate, endDate, page) = SearchConfig().apply(block)
    check(searchTarget in listOf(EXACT_MATCH_FOR_TAGS, PARTIAL_MATCH_FOR_TAGS, TITLE_AND_CAPTION)) {
        "searchTarget must be EXACT_MATCH_FOR_TAGS, PARTIAL_MATCH_FOR_TAGS or TITLE_AND_CAPTION"
    }
    val userInfo = getCurrentUserSimpleProfile()
    if (!userInfo.isPremium && sort == POPULAR_DESC) {
        if (page != 1) {
            return emptyList()
        }
        return client.get("v1/search/popular-preview/illust") {
            parameter("filter", "for_android")
            parameter("include_translated_tag_results", true)
            parameter("merge_plain_keyword_results", true)
            parameter("word", word)
            parameter("search_target", searchTarget.name.lowercase())
            if (startDate != null) {
                parameter("start_date", startDate.toString())
            }
            if (endDate != null) {
                parameter("end_date", endDate.toString())
            }
        }.body<IllustResult>().illusts

    }
    val body = client.get("v1/search/illust") {
        parameter("filter", "for_android")
        parameter("include_translated_tag_results", true)
        parameter("word", word)
        parameter("sort", sort.name.lowercase())
        parameter("search_target", searchTarget.name.lowercase())
        if (startDate != null) {
            parameter("start_date", startDate.toString())
        }
        if (endDate != null) {
            parameter("end_date", endDate.toString())
        }
        parameter("offset", (page - 1) * 30)
    }
    return body.body<IllustResult>().illusts
}
