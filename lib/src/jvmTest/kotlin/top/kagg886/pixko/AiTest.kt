package top.kagg886.pixko

import kotlinx.coroutines.runBlocking
import top.kagg886.pixko.module.ai.isAllowedAiShow
import top.kagg886.pixko.module.ai.setAllowedAiShow
import kotlin.test.BeforeTest
import kotlin.test.Test

class AiTest {
    @Test
    fun testAiShow():Unit = runBlocking {
        println(client.isAllowedAiShow())
    }

    @Test
    fun testAiShowSetting():Unit = runBlocking {
        println(client.setAllowedAiShow(false))
    }



    companion object {
        lateinit var client: PixivAccount

        @BeforeTest
        fun preparePixivClient() {
            client = AuthTest.generatePixivAccount()
        }
    }
}
