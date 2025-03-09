package top.kagg886.pixko

import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.ai.isAllowedAiShow
import top.kagg886.pixko.module.ai.setAllowedAiShow
import kotlin.test.Test

class AiTest : TestWithClient() {
    @Test
    fun testAiShow() = runTest {
        println(client.isAllowedAiShow())
    }

    @Test
    fun testAiShowSetting() = runTest {
        println(client.setAllowedAiShow(false))
    }
}
