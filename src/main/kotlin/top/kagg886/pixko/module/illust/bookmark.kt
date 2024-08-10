package top.kagg886.pixko.module.illust

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import top.kagg886.pixko.PixivAccount

enum class BookmarkVisibility {
    PUBLIC, PRIVATE
}

class BookmarkOptions {
    var visibility: BookmarkVisibility = BookmarkVisibility.PUBLIC
}

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
suspend fun PixivAccount.deleteBookmarkIllust(illustId: Long): Boolean {
    val resp = client.post("v2/illust/bookmark/delete") {
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