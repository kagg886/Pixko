package top.kagg886.pixko.module

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.utils.io.jvm.javaio.*
import top.kagg886.pixko.PixivAccount
import java.io.InputStream
import java.io.OutputStream

suspend fun PixivAccount.loadImage(url: String): InputStream {
    return client.prepareGet(url) {
        headers.append("Referer","https://www.pixiv.net/")
    }.execute {
        it.bodyAsChannel().toInputStream()
    }
}

private class TransferInputStream(val out: OutputStream) : InputStream() {
    override fun read(): Int {
        TODO("Not yet implemented")
    }

    override fun read(b: ByteArray): Int {
        return super.read(b)
    }

    override fun read(b: ByteArray, off: Int, len: Int): Int {
        return super.read(b, off, len)
    }

    override fun readAllBytes(): ByteArray {
        return super.readAllBytes()
    }

    override fun readNBytes(len: Int): ByteArray {
        return super.readNBytes(len)
    }

    override fun readNBytes(b: ByteArray?, off: Int, len: Int): Int {
        return super.readNBytes(b, off, len)
    }
}