package top.kagg886.pixko.module.novel

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.BookmarkOptions
/**
 * # 添加小说收藏
 * @param novelId 小说id
 * @param block 收藏设置
 */
suspend fun PixivAccount.bookmarkNovel(novelId: Long, block: BookmarkOptions.() -> Unit = {}): Boolean {
    val options = BookmarkOptions().apply(block)
    val resp = client.post("v2/novel/bookmark/add") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    append("novel_id", novelId.toString())
                    append("restrict", options.visibility.name.lowercase())
                }
            )
        )
    }
    return resp.status == HttpStatusCode.OK
}

/**
 * # 删除小说收藏
 * @param novelId 小说id
 *
 *
 */
suspend fun PixivAccount.deleteBookmarkNovel(novelId: Long): Boolean {
    val resp = client.post("v1/novel/bookmark/delete") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    append("novel_id", novelId.toString())
                }
            )
        )
    }
    return resp.status == HttpStatusCode.OK && resp.bodyAsText().contains("error").not()
}