package top.kagg886.pixko.module.illust

import top.kagg886.pixko.PixivAccount
import io.ktor.client.call.*
import io.ktor.client.request.*

suspend fun PixivAccount.getRecommendIllust(): IllustResult {
    return getRecommendIllustNext("v1/illust/recommended?include_privacy_policy=true&filter=for_android&include_ranking_illusts=true")
}

suspend fun PixivAccount.getRecommendIllustNext(url: String): IllustResult {
    return client.get(url).body()
}