import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount

class UserTest {
    companion object {
        lateinit var client: PixivAccount

        @JvmStatic
        @BeforeAll
        fun preparePixivClient() {
            client = AuthTest.generatePixivAccount()
        }
    }
}