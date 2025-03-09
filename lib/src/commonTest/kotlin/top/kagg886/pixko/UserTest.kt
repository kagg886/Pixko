package top.kagg886.pixko

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonElement
import top.kagg886.pixko.internal.json
import top.kagg886.pixko.module.illust.IllustResult
import top.kagg886.pixko.module.illust.NovelResult
import top.kagg886.pixko.module.profile.CountryCode
import top.kagg886.pixko.module.profile.JapanAddress
import top.kagg886.pixko.module.user.*
import kotlin.test.Test

class UserTest : TestWithClient() {
    @Test
    fun testProfile() = runTest {
        val state = client.getCurrentUserSimpleProfile()
        println(state)
    }

    @Test
    fun testUserIllusts() = runTest {
        val state = client.getUserIllust(20235288)
        println(state)
    }

    @Test
    fun testUserDetail() = runTest {
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
    fun testPreset() = runTest {
        val state = client.client.get("v1/user/profile/presets")
        println(json.decodeFromString<JsonElement>(state.bodyAsText()))
    }

    @Test
    fun testUserWorkSpaceEdit() = runTest {
        val userId = client.getCurrentUserSimpleProfile().userId
        val workspace = client.getUserInfo(userId).workspace

        client.setUserWorkSpace(workspace.copy(pc = "Windows 11"))
    }

    @Test
    fun testUserProfileEdit() = runTest {
        val userId = client.getCurrentUserSimpleProfile().userId
        val profile = client.getUserInfo(userId).buildSettings {
            country = CountryCode.CHINA
            address = JapanAddress.INTERNATIONAL

//            avatar = BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB).run {
//                val stream = ByteArrayOutputStream()
//                this.createGraphics().apply {
//                    paint = java.awt.Color.RED
//                    drawRect(20, 20, 60, 60)
//                    dispose()
//                }
//                assertTrue { ImageIO.write(this, "PNG", stream) }
//
//                ByteArrayInputStream(stream.toByteArray())
//            }
        }
        client.setUserProfile(profile)
    }

    @Test
    fun testBookmarkTagsList() = runTest {
        val tags = client.getAllFavoriteTags(
            restrict = UserLikePublicity.PRIVATE,
            favoriteTagsType = FavoriteTagsType.Illust
        )
        println(tags)
    }

    @Test
    fun testBookmarkIllustList() = runTest {
        val userId = client.getCurrentUserSimpleProfile().userId
        var a: IllustResult? = client.getUserLikeIllust(userId)
        while (a != null) {
            println(a)
            a = client.getUserLikeIllustNext(a) ?: break
        }
    }

    @Test
    fun testBookmarkIllustListWithTags() = runTest {
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
    fun testBookmarkNovelList() = runTest {
        val userId = client.getCurrentUserSimpleProfile().userId
        var a: NovelResult? = client.getUserLikeNovel(userId)
        while (a != null) {
            println(a)
            a = client.getUserLikeNovelNext(a) ?: break
        }
    }

    @Test
    fun testUserUnFollow() = runTest {
        client.unFollowUser(13379747)
    }

    @Test
    fun testUserFollow() = runTest {
        client.followUser(13379747)
    }
    @Test
    fun testUserFollowList() = runTest {
        println(client.getFollowingList(13379747))
    }

    @Test
    fun testUserNovel() = runTest {
        println(client.getUserNovel(22919310))
    }

    @Test
    fun testUserNovelSeries() = runTest {
        println(client.getNovelSeries(1))
    }
}
