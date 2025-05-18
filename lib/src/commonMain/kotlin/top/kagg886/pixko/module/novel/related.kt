package top.kagg886.pixko.module.novel

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount

/**
 * 获取相关小说
 * @param novelId 小说ID
 * @return 相关小说列表
 */
suspend fun PixivAccount.getRelatedNovel(novelId: Long): NovelResult {
    return client.get("v1/novel/related") {
        parameter("novel_id", novelId)
    }.body<NovelResult>()
}

/**
 * 获取下一页相关小说
 * @param result 上一页的结果
 * @return 下一页的结果
 */
suspend fun PixivAccount.getRelatedNovelNext(result: NovelResult): NovelResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl).body()
}
