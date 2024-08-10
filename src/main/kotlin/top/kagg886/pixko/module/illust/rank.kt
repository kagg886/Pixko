package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount

enum class RankCategory(val content: String) {
    DAY("day"), //日榜
    WEEK("week"), //周榜
    MONTH("month"),//月榜
    MALE("day_male"),//男性
    FEMALE("day_female"),//女性
    ORIGINAL("week_original"),//原创
    FRESHMAN("week_rookie"),//新人
}

suspend fun PixivAccount.getRankIllust(mode: RankCategory, page: Int = 1): IllustResult {
    return client.get("v1/illust/ranking") {
        parameter("filter", "for_android")
        parameter("mode", mode.content)
        parameter("date", "")
        parameter("offset", (page - 1) * 30)
//        parameter("date", year + "-" + (monthOfYear + 1) + "-" + dayOfMonth)

    }.body()
}