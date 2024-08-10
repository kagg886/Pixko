package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount

suspend fun PixivAccount.getRecommendIllust(): IllustResult {
    return client.get("v1/illust/recommended?include_privacy_policy=true&filter=for_android&include_ranking_illusts=true")
        .body()
}