package top.kagg886.pixko.module.search

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.IllustResult
import top.kagg886.pixko.module.illust.NovelResult
import top.kagg886.pixko.module.novel.Novel
import top.kagg886.pixko.module.search.SearchSort.POPULAR_DESC
import top.kagg886.pixko.module.user.getCurrentUserSimpleProfile

//    /**
//     * search_target=exact_match_for_tags,partial_match_for_tags,text(文本),keyword(关键词)
//     */
//    @GET("v1/search/novel?filter=for_android&include_translated_tag_results=true&merge_plain_keyword_results=true")
//    Observable<ListNovel> searchNovel(@Header("Authorization") String token,
//                                      @Query("word") String word,
//                                      @Query("sort") String sort,
//                                      @Query("start_date") String startDate,
//                                      @Query("end_date") String endDate,
//                                      @Query("search_target") String search_target);

/**
 * # 搜索小说
 * @param word 搜索关键词
 * @param block 搜索配置
 * @return [IllustResult] 搜索结果
 */
suspend fun PixivAccount.searchNovel(
    word: String,
    block: SearchConfig.() -> Unit = {}
): List<Novel> {
    val (sort, searchTarget, startDate, endDate, page) = SearchConfig().apply(block)
    check(
        searchTarget in listOf(
            SearchTarget.EXACT_MATCH_FOR_TAGS,
            SearchTarget.PARTIAL_MATCH_FOR_TAGS,
            SearchTarget.TEXT,
            SearchTarget.KEYWORD
        )
    ) {
        "searchTarget must be EXACT_MATCH_FOR_TAGS,PARTIAL_MATCH_FOR_TAGS,TEXT,KEYWORD"
    }
    val userInfo = getCurrentUserSimpleProfile()
    if (!userInfo.isPremium && sort == POPULAR_DESC) {
        if (page != 1) {
            return emptyList()
        }
        return client.get("v1/search/popular-preview/novel") {
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
        }.body<NovelResult>().novels
    }


    val body = client.get("v1/search/novel") {
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
    return body.body<NovelResult>().novels
}
