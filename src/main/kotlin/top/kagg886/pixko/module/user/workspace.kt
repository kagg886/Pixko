package top.kagg886.pixko.module.user

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import top.kagg886.pixko.PixivAccount
import kotlin.reflect.full.memberProperties


/**
 * # 设置用户工作空间简介
 * @property workspace 工作空间简介
 *
 * @see UserWorkspace
 */
suspend fun PixivAccount.setUserWorkSpace(workspace: UserWorkspace) {
    client.post("v1/user/workspace/edit") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(
            FormDataContent(
                Parameters.build {
                    UserWorkspace::class.memberProperties.forEach {
                        append(it.name, it.get(workspace).toString())
                    }
                }
            )
        )
    }
}