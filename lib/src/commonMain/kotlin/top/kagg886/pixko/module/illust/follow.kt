package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.IllustFollowPublicity.*

/**
 * # 用户关注的插画时间流类型
 * @property PUBLIC 公开
 * @property PRIVATE 私密
 * @property ALL 全部
 */
enum class IllustFollowPublicity {
    PUBLIC, PRIVATE, ALL
}

/**
 * # 获取用户关注的插画时间流选项
 * @property page 页码，默认值为1
 * @property publicity 公开类型，默认值为ALL
 * @see IllustFollowPublicity
 */
class IllustFollowOptions {
    var page = 1
    var publicity = ALL
}

/**
 * # 获取用户关注的插画时间流
 * @param page 页码，默认值为1
 * @return [List] 插画列表
 */
suspend fun PixivAccount.getIllustFollowList(block: IllustFollowOptions.() -> Unit = {}): List<Illust> {
    val option = IllustFollowOptions().apply(block)
    return client.get("v2/illust/follow") {
        parameter("offset", (option.page - 1) * 30)
        parameter("restrict", option.publicity.name.lowercase())
    }.body<IllustResult>().illusts
}
