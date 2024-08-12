package top.kagg886.pixko.module.search

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.Tag
/**
 * # 搜索tag
 * @param word 要搜索的tag
 * @return [List] 搜索结果
 */
suspend fun PixivAccount.searchTag(word: String): List<Tag> {
    @Serializable
    data class WrapTag(
        val tags: List<Tag>
    )
    return client.get("v2/search/autocomplete") {
        parameter("merge_plain_keyword_results", true)
        parameter("word", word)
    }.body<WrapTag>().tags
}