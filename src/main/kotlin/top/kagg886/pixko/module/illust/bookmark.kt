package top.kagg886.pixko.module.illust

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import top.kagg886.pixko.PixivAccount

/**
 * # 收藏可见性
 */
enum class BookmarkVisibility {
    PUBLIC, PRIVATE
}

/**
 * # 收藏设置
 * @property visibility 可见性
 */
class BookmarkOptions {
    var visibility: BookmarkVisibility = BookmarkVisibility.PUBLIC
}

/**
 * # 添加插画收藏
 * @param illustId 插画id
 * @param block 收藏设置
 *
 * 
 */
suspend fun PixivAccount.bookmarkIllust(illustId: Long, block: BookmarkOptions.() -> Unit = {}): Boolean {
    val options = BookmarkOptions().apply(block)
    val resp = client.post("v2/illust/bookmark/add") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    append("illust_id", illustId.toString())
                    append("restrict", options.visibility.name.lowercase())
                }
            )
        )
    }
    return resp.status == HttpStatusCode.OK
}

/**
 * # 删除插画收藏
 * @param illustId 插画id
 *
 * 
 */
suspend fun PixivAccount.deleteBookmarkIllust(illustId: Long): Boolean {
    val resp = client.post("v1/illust/bookmark/delete") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    append("illust_id", illustId.toString())
                }
            )
        )
    }
    return resp.status == HttpStatusCode.OK && resp.bodyAsText().contains("error").not()
}