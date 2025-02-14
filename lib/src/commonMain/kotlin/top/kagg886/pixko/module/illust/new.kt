package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount

suspend fun PixivAccount.getLatestIllust(): IllustResult {
    return client.get("/v1/illust/new?filter=for_android&content_type=illust")
        .body<IllustResult>()
}

suspend fun PixivAccount.getLatestIllustNext(result: IllustResult): IllustResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl)
        .body<IllustResult>()
}
