package top.kagg886.pixko.module.novel

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount

/**
 * # 获取小说推荐详情
 * @return [NovelResult] 小说推荐详情
 *
 */
suspend fun PixivAccount.getRecommendNovel(): NovelResult {
    return client.get("v1/novel/recommended?include_privacy_policy=true&filter=for_android&include_ranking_Novels=true")
        .body()
}

/**
 * # 获取小说推荐详情的下一页
 * @param result 当前页的小说推荐详情
 * @return [NovelResult] 下一页的小说推荐详情，如果没有下一页则返回null
 *
 */
suspend fun PixivAccount.getRecommendNovelNext(result: NovelResult): NovelResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl).body()
}