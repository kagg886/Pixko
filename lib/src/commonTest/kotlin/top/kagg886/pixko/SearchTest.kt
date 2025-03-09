package top.kagg886.pixko
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import top.kagg886.pixko.module.search.*
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

class SearchTest : TestWithClient() {
    @Test
    fun testSearchNovelCustomConfig() = runTest {
        for (i in 1..3) {
            client.searchNovel("米拉") {
                page = i
                searchTarget = SearchTarget.TEXT
            }.apply {
                println(this)
            }
        }

        delay(1.minutes)

    }

    @Test
    fun testSearchNovel() = runTest {
        for (i in 1..3) {
            client.searchNovel("AI") {
                page = i
            }.apply {
                println(this)
            }
        }
    }
    @Test
    fun testSearchIllust() = runTest {
        for (i in 1..3) {
            client.searchIllust("原神") {
                page = i
            }.apply {
                println(this)
            }
        }
    }

    @Test
    fun testSearchUserIllust() = runTest {
        val dm = client.searchUser("呆猫", 100)
        val dm2 = client.searchUser("呆猫", 100)
        println(dm)
        println(dm2)
    }

    @Test
    fun testSearchTag() = runTest {
        println(client.searchTag("长筒"))
    }

    @Test
    fun testSearchUser() = runTest {
        println(
            client.searchUser("a").size
        )
    }
}
