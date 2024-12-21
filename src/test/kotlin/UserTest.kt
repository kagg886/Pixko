import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.JsonElement
import org.junit.jupiter.api.BeforeAll
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.Tag
import top.kagg886.pixko.internal.json
import top.kagg886.pixko.module.illust.IllustResult
import top.kagg886.pixko.module.illust.NovelResult
import top.kagg886.pixko.module.profile.CountryCode
import top.kagg886.pixko.module.profile.JapanAddress
import top.kagg886.pixko.module.user.*
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO
import kotlin.test.Test
import kotlin.test.assertTrue

class UserTest {

    @Test
    fun testProfile(): Unit = runBlocking {
        val state = client.getCurrentUserSimpleProfile()
        println(state)
    }

    @Test
    fun testUserIllusts():Unit = runBlocking {
        val state = client.getUserIllust(20235288)
        println(state)
    }

    @Test
    fun testUserDetail(): Unit = runBlocking {
        val state = client.getUserInfo(38000473)
        println(state)
        with(state) {
            println(profile)
            println(profile.job)
            println(profile.country)
            println(profile.address)
        }
    }

    @Test
    fun testPreset(): Unit = runBlocking {
        val state = client.client.get("v1/user/profile/presets")
        println(json.decodeFromString<JsonElement>(state.bodyAsText()))
    }

    @Test
    fun testUserWorkSpaceEdit(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        val workspace = client.getUserInfo(userId).workspace

        client.setUserWorkSpace(workspace.copy(pc = UUID.randomUUID().toString()))
    }

    @Test
    fun testUserProfileEdit(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        val profile = client.getUserInfo(userId).buildSettings {
            country = CountryCode.CHINA
            address = JapanAddress.INTERNATIONAL

            avatar = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).run {
                val stream = ByteArrayOutputStream()
                this.createGraphics().apply {
                    paint = java.awt.Color.RED
                    drawRect(20, 20, 60, 60)
                    dispose()
                }
                assertTrue { ImageIO.write(this, "PNG", stream) }

                ByteArrayInputStream(stream.toByteArray())
            }
        }
        client.setUserProfile(profile)
    }

    @Test
    fun testBookmarkTagsList(): Unit = runBlocking {
        val tags = client.getAllFavoriteTags(
            restrict = UserLikePublicity.PRIVATE,
            favoriteTagsType = FavoriteTagsType.Illust
        )
        println(tags)
    }

    @Test
    fun testBookmarkIllustList(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        var a: IllustResult? = client.getUserLikeIllust(userId)
        while (a != null) {
            println(a)
            a = client.getUserLikeIllustNext(a) ?: break
        }
    }

    @Test
    fun testBookmarkIllustListWithTags(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        var a: IllustResult? = client.getUserLikeIllust(
            userId,
            filter = TagFilter.FilterWithTag(Tag("ソックス足裏"))
        )
        while (a != null) {
            println(a)
            a = client.getUserLikeIllustNext(a) ?: break
        }
    }

    @Test
    fun testBookmarkNovelList(): Unit = runBlocking {
        val userId = client.getCurrentUserSimpleProfile().userId
        var a: NovelResult? = client.getUserLikeNovel(userId)
        while (a != null) {
            println(a)
            a = client.getUserLikeNovelNext(a) ?: break
        }
    }

    @Test
    fun testUserUnFollow():Unit = runBlocking {
        client.unFollowUser(13379747)
    }

    @Test
    fun testUserFollow():Unit = runBlocking {
        client.followUser(13379747)
    }
    @Test
    fun testUserFollowList():Unit = runBlocking {
        println(client.getFollowingList(13379747))
    }

    @Test
    fun testUserNovel():Unit = runBlocking {
        println(client.getUserNovel(22919310))
    }

    companion object {
        lateinit var client: PixivAccount

        @JvmStatic
        @BeforeAll
        fun preparePixivClient() {
            client = AuthTest.generatePixivAccount()
        }
    }
}