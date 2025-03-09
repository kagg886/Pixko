package top.kagg886.pixko

import io.ktor.client.engine.js.*

actual object TestPixivAccountFactory {
    actual fun newAccountFromConfig(block: PixivAccountConfig<*>.() -> Unit): PixivAccount =
        PixivAccountFactory.newAccountFromConfig(Js, block)

    actual fun newAccount(verify: String): PixivVerification<*> =
        PixivAccountFactory.newAccount(Js, verify)

    actual fun newAccount(): PixivVerification<*> =
        PixivAccountFactory.newAccount(Js)
}
