package top.kagg886.pixko

import io.ktor.client.engine.okhttp.*

actual object TestPixivAccountFactory {
    actual fun newAccountFromConfig(block: PixivAccountConfig<*>.() -> Unit): PixivAccount =
        PixivAccountFactory.newAccountFromConfig(OkHttp, block)

    actual fun newAccount(verify: String): PixivVerification<*> =
        PixivAccountFactory.newAccount(OkHttp, verify)

    actual fun newAccount(): PixivVerification<*> =
        PixivAccountFactory.newAccount(OkHttp)
}