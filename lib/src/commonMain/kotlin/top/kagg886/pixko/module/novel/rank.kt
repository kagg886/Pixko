package top.kagg886.pixko.module.novel

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount

//{"day", "week","day_male", "day_female", "week_rookie", "day_r18"};

enum class RankCategory(val content: String) {
    DAY("day"), //日榜
    WEEK("week"), //周榜
    MALE("day_male"),//男性
    FEMALE("day_female"),//女性
    FRESHMAN("week_rookie"),//新人
    DAY_R18("day_r18"), //R18日榜
}

/**
 * # 获取小说榜中某一分区的内容
 * @param mode 排行榜类型
 * @param page 页码
 * @return [List]
 *
 */
suspend fun PixivAccount.getRankNovel(mode: RankCategory, page: Int = 1): List<Novel> {
    return client.get("v1/novel/ranking") {
        parameter("filter", "for_android")
        parameter("mode", mode.content)
        parameter("date", "")
        parameter("offset", (page - 1) * 30)
    }.body<NovelResult>().novels
}
