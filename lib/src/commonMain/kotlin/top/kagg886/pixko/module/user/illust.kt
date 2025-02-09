package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.Illust
import top.kagg886.pixko.module.illust.IllustResult

//    @GET("v1/user/illusts?filter=for_android")
//    Observable<ListIllust> getUserSubmitIllust(@Header("Authorization") String token,
//                                               @Query("user_id") int user_id,
//                                               @Query("type") String type);

/**
 * # 获取用户上传的插画
 * @param userId 用户ID
 * @param page 页码
 * @return [List] 返回结果
 */
suspend fun PixivAccount.getUserIllust(userId: Long, page: Int = 1): List<Illust> {
    return client.get("v1/user/illusts") {
        parameter("filter", "for_android")
        parameter("user_id", userId)
        parameter("type", "illust")
        parameter("offset", (page - 1) * 30)
    }.body<IllustResult>().illusts
}