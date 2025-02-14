package top.kagg886.pixko.module.user

import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonPrimitive
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json


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
                    val param = json.encodeToJsonElement(workspace) as JsonObject

                    for ((k,v) in param) {
                        v.jsonPrimitive.contentOrNull?.let {
                            append(k,it)
                        }
                    }
                }
            )
        )
    }
}
