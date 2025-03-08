package top.kagg886.pixko

import kotlin.test.BeforeTest

abstract class TestWithClient {
    lateinit var client: PixivAccount

    @BeforeTest
    fun preparePixivClient() {
        client = AuthTest.generatePixivAccount()
    }
}

// 为特定平台创建特定engine的account
expect object TestPixivAccountFactory {
    fun newAccountFromConfig(block: PixivAccountConfig<*>.() -> Unit = {}): PixivAccount
    fun newAccount(verify: String): PixivVerification<*>
    fun newAccount(): PixivVerification<*>
}



