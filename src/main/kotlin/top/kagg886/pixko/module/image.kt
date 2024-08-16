package top.kagg886.pixko.module

import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import top.kagg886.pixko.PixivAccount

suspend fun PixivAccount.loadImage(url: String): ByteArray {
    return client.get(url) {
        timeout {
            requestTimeoutMillis = 60000
            socketTimeoutMillis = 60000
            connectTimeoutMillis = 60000
        }
        headers.append("Referer", "https://www.pixiv.net/")
    }.bodyAsChannel().toInputStream().readBytes()
}