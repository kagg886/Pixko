package top.kagg886.pixko.module.novel

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.novel.NovelFollowPublicity.*

/**
 * # 用户关注的小说时间流类型
 * @property PUBLIC 公开
 * @property PRIVATE 私密
 * @property ALL 全部
 */
enum class NovelFollowPublicity {
    PUBLIC, PRIVATE, ALL
}

/**
 * # 获取用户关注的小说时间流选项
 * @property page 页码，默认值为1
 * @property publicity 公开类型，默认值为ALL
 * @see NovelFollowPublicity
 */
class NovelFollowOptions {
    var page = 1
    var publicity = NovelFollowPublicity.ALL
}

/**
 * # 获取用户关注的小说时间流
 * @param block 配置
 * @return [List] 小说列表
 */
suspend fun PixivAccount.getIllustFollowList(block: NovelFollowOptions.() -> Unit = {}): List<Novel> {
    val option = NovelFollowOptions().apply(block)
    return client.get("v2/novel/follow") {
        parameter("offset", (option.page - 1) * 30)
        parameter("restrict", option.publicity.name.lowercase())
    }.body<NovelResult>().novels
}
