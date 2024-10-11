package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.NovelResult
import top.kagg886.pixko.module.novel.Novel

/**
 * # 获取用户上传的小说
 * @param userId 用户ID
 * @param page 页码
 * @return [List] 返回结果
 */
suspend fun PixivAccount.getUserNovel(userId: Long, page: Int = 1): List<Novel> {
    return client.get("v1/user/novels") {
        parameter("filter", "for_android")
        parameter("user_id", userId)
        parameter("offset", (page - 1) * 30)
    }.body<NovelResult>().run {
        novels
    }
}