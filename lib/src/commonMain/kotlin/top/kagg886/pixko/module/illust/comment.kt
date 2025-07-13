package top.kagg886.pixko.module.illust

import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.User
import kotlin.properties.Delegates
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Serializable
internal data class CommentListResult(
    @SerialName("comments")
    val comments: List<Comment>,
    @SerialName("next_url")
    val nextUrl: String?,
)

/**
 * # 评论
 *
 * @property id 评论id
 * @property comment 评论内容
 * @property date 评论时间
 * @property user 评论用户
 * @property hasReplies 是否有回复，若该评论本身为回复则永远为false
 * @property stamp 评论的大表情，该项不为null时，comment为空
 */
@OptIn(ExperimentalTime::class)
@Serializable
data class Comment(
    val id: Long,
    val comment: String,
    @Contextual
    val date: Instant,
    val user: User,
    @SerialName("has_replies")
    val hasReplies: Boolean,
    val stamp: SuperFace?, //超级表情
)

/**
 * # 超级表情
 *
 * @property url 超级表情的url
 */
@Serializable
data class SuperFace(
    @SerialName("stamp_url")
    val url: String,
)

/**
 * # 获取评论列表
 *
 * @param illustId 作品id
 * @param page 页码，默认值为1
 * @return 评论列表
 *
 *
 */
suspend fun PixivAccount.getIllustComment(illustId: Long, page: Int = 1): List<Comment> {
    return client.get("v3/illust/comments") {
        parameter("illust_id", illustId)
        parameter("offset", (page - 1) * 30)
    }.body<CommentListResult>().comments
}

/**
 * # 获取回复列表
 *
 * @param commentId 评论id
 * @param page 页码，默认值为1
 * @return 回复列表
 *
 */
suspend fun PixivAccount.getIllustCommentReply(commentId: Long, page: Int = 1): List<Comment> {
    return client.get("v2/illust/comment/replies") {
        parameter("comment_id", commentId)
        parameter("offset", (page - 1) * 30)
    }.body<CommentListResult>().comments
}

/**
 * # 发送评论的配置
 *
 * @property illustId 作品id
 * @property comment 评论内容
 * @property parentCommentId 父评论id，若为null则表示发送回复，否则为发送评论
 */
class IllustComment {
    var illustId by Delegates.notNull<Long>()
    var comment by Delegates.notNull<String>()
    var parentCommentId: Long? = null
}

/**
 * # 发送评论
 *
 * @param block 配置
 * @return 发送的评论配置
 *
 *
 */
suspend fun PixivAccount.sendIllustComment(block: IllustComment.() -> Unit): Comment {
    @Serializable
    data class CommentHolder(
        val comment: Comment
    )

    val illustComment = IllustComment().apply(block)

    val resp = client.post("v1/illust/comment/add") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(FormDataContent(
            Parameters.build {
                append("illust_id", illustComment.illustId.toString())
                append("comment", illustComment.comment)
                illustComment.parentCommentId?.let {
                    append("parent_comment_id", it.toString())
                }
            }
        ))
    }.body<CommentHolder>()
    return resp.comment
}

/**
 * # 删除评论
 *
 * @param commentId 评论id
 *
 */
suspend fun PixivAccount.deleteIllustComment(commentId: Long) {
    client.post("v1/illust/comment/delete") {
        contentType(ContentType.Application.FormUrlEncoded)
        setBody(FormDataContent(
            Parameters.build {
                append("comment_id", commentId.toString())
            }
        ))
    }
}
