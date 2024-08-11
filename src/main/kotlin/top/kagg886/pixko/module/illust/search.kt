package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDateTime
import top.kagg886.pixko.PixivAccount

/**
 * 搜索结果
 * @property DESC 降序
 * @property ASC 升序
 */
enum class SearchSort {
    DESC,
    ASC
}

/**
 * # 搜索目标
 * @property EXACT_MATCH_FOR_TAGS 精确匹配标签
 * @property PARTIAL_MATCH_FOR_TAGS 模糊匹配标签
 * @property TITLE_AND_CAPTION 标题和描述
 */
enum class SearchTarget {
    EXACT_MATCH_FOR_TAGS,
    PARTIAL_MATCH_FOR_TAGS,
    TITLE_AND_CAPTION
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
    var sort: SearchSort = SearchSort.DESC,
    var searchTarget: SearchTarget = SearchTarget.PARTIAL_MATCH_FOR_TAGS,
    var startDate: LocalDateTime? = null,
    var endDate: LocalDateTime? = null,
    var page: Int = 1
)

/**
 * # 搜索图片
 * @param word 搜索关键词
 * @param block 搜索配置
 * @return [IllustResult] 搜索结果
 * @sample IllustTest.testSearch
 */
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