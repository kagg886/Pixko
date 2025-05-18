package top.kagg886.pixko.module.illust

import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import top.kagg886.pixko.PixivAccount

/**
 * 获取相关插画
 * @param illustId 插画ID
 * @return 相关插画列表
 */
suspend fun PixivAccount.getRelatedIllust(illustId: Long): IllustResult {
    return client.get("v2/illust/related") {
        parameter("illust_id", illustId)
    }.body<IllustResult>()
}

/**
 * 获取下一页相关插画
 * @param result 上一页的结果
 * @return 下一页的结果
 */
suspend fun PixivAccount.getRelatedIllustNext(result: IllustResult): IllustResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl).body()
}
