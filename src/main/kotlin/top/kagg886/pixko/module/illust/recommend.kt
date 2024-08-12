package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount

/**
 * # 插画推荐详情
 * @property nextUrl 下一页的url
 * @property illusts 插画列表
 */
@Serializable
data class IllustResult(
    @SerialName("next_url")
    val nextUrl: String?,
    val illusts: List<Illust>
)

/**
 * # 获取插画推荐详情
 * @return [IllustResult] 插画推荐详情
 *
 */
suspend fun PixivAccount.getRecommendIllust(): IllustResult {
    return client.get("v1/illust/recommended?include_privacy_policy=true&filter=for_android&include_ranking_illusts=true")
        .body()
}

/**
 * # 获取插画推荐详情的下一页
 * @param result 当前页的插画推荐详情
 * @return [IllustResult] 下一页的插画推荐详情，如果没有下一页则返回null
 *
 */
suspend fun PixivAccount.getRecommendIllustNext(result: IllustResult): IllustResult? {
    return if (result.nextUrl == null) null else client.get(result.nextUrl).body()
}