package top.kagg886.pixko

import io.ktor.client.statement.*
import java.io.IOException

data class PixivException(
    val url: String,
    val resp: HttpResponse,
    override val cause: Throwable? = null
) : IOException()