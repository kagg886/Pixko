package top.kagg886.pixko
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.test.BeforeTest
import top.kagg886.pixko.module.search.*
import kotlin.test.Test
import kotlin.time.Duration.Companion.minutes

class SearchTest {
    lateinit var client: PixivAccount
    @BeforeTest
    fun preparePixivClient() {
        client = AuthTest.generatePixivAccount()
    }
    @Test
    fun testSearchNovelCustomConfig(): Unit = runBlocking {
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
    fun testSearchNovel(): Unit = runBlocking {
        for (i in 1..3) {
            client.searchNovel("AI") {
                page = i
            }.apply {
                println(this)
            }
        }
    }
    @Test
    fun testSearchIllust(): Unit = runBlocking {
        for (i in 1..3) {
            client.searchIllust("原神") {
                page = i
            }.apply {
                println(this)
            }
        }
    }

    @Test
    fun testSearchUserIllust(): Unit = runBlocking {
        val dm = client.searchUser("呆猫", 100)
        val dm2 = client.searchUser("呆猫", 100)
        println(dm)
        println(dm2)
    }

    @Test
    fun testSearchTag():Unit = runBlocking {
        println(client.searchTag("长筒"))
    }

    @Test
    fun testSearchUser(): Unit = runBlocking {
        println(
            client.searchUser("a").size
        )
    }
}
