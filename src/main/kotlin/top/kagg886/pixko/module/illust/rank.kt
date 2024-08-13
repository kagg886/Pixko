package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.RankCategory.*

/**
 * # 排行榜类型
 * @property DAY 日榜
 * @property WEEK 周榜
 * @property MONTH 月榜
 * @property MALE 男性
 * @property FEMALE 女性
 * @property ORIGINAL 原创
 * @property FRESHMAN 新人
 */
enum class RankCategory(val content: String) {
    DAY("day"), //日榜
    WEEK("week"), //周榜
    MONTH("month"),//月榜
    MALE("day_male"),//男性
    FEMALE("day_female"),//女性
    ORIGINAL("week_original"),//原创
    FRESHMAN("week_rookie"),//新人
}

/**
 * # 获取插画榜单中某一分区的内容
 * @param mode 排行榜类型
 * @param page 页码
 * @return [List]
 * 
 */
suspend fun PixivAccount.getRankIllust(mode: RankCategory, page: Int = 1): List<Illust> {
    return client.get("v1/illust/ranking") {
        parameter("filter", "for_android")
        parameter("mode", mode.content)
        parameter("date", "")
        parameter("offset", (page - 1) * 30)
//        parameter("date", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth)

    }.body<IllustResult>().illusts
}