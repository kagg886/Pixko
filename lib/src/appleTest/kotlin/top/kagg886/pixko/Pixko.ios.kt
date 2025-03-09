package top.kagg886.pixko

import io.ktor.client.engine.darwin.*

actual object TestPixivAccountFactory {
    actual fun newAccountFromConfig(block: PixivAccountConfig<*>.() -> Unit): PixivAccount =
        PixivAccountFactory.newAccountFromConfig(Darwin, block)

    actual fun newAccount(verify: String): PixivVerification<*> =
        PixivAccountFactory.newAccount(Darwin, verify)

    actual fun newAccount(): PixivVerification<*> =
        PixivAccountFactory.newAccount(Darwin)
}