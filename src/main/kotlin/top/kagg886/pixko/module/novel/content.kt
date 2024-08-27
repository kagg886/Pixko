package top.kagg886.pixko.module.novel

import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.internal.json
import top.kagg886.pixko.module.novel.parser.createNovelData

//{
//    "id": "19713880",
//    "title": "友奈vs若葉 (ai小说测试)",
//    "seriesId": null,
//    "seriesTitle": null,
//    "seriesIsWatched": null,
//    "userId": "29782420",
//    "coverUrl": "https:\/\/i.pximg.net\/c\/240x480_80\/novel-cover-master\/img\/2023\/04\/18\/16\/01\/21\/ci19713880_2f875f0a416bafccf6acc931048f08ca_master1200.jpg",
//    "tags": [
//        "R-18",
//        "ボクシング",
//        "格闘",
//        "キャットファイト",
//        "女子ボクシング",
//        "胸パン",
//        "空手",
//        "リョナ"
//    ],
//    "caption": "久违的投稿了一次小说。用“科幻小说”的名义才说服gpt生成“鼓励暴力的不安全内容”。",
//    "cdate": "2023-04-18",
//    "rating": {
//        "like": 16,
//        "bookmark": 27,
//        "view": 888
//    },
//    "text": "友奈和若叶两位女生在比赛中紧紧盯着对方，等待着最佳的进攻时机。突然，友奈发动了一次猛烈的攻击，直取若叶的鸠尾。若叶连忙闪避，但被友奈的拳头擦过，差点没能避开。她立即反击，向友奈的腹部发动了一次重击，但被友奈及时格挡了下来。\n\n友奈趁机再次进攻，这次她瞄准的是若叶的胸部，若叶来不及躲避，只得硬生生地承受了这一击。然而，若叶并没有被击倒，她稳住身体，迅速向友奈反击，一记狠狠的拳头正中友奈的心脏。友奈倒退几步，但很快又稳住了身体。\n\n两人又开始缠斗，交错着攻击和防御，不断试图寻找对方的破绽。友奈再次发起进攻，但这次被若叶迅速闪避，友奈的拳头只擦过了若叶的胸部。若叶趁机还击，这次攻击直取友奈的腹部。友奈尽管用力格挡，但还是被若叶的攻击震得有些站立不稳。\n\n场上的气氛越来越紧张，两人都知道只要稍有疏忽，就有可能输掉比赛。\n\n在友奈瞄准若叶的胸部发动攻击时，若叶来不及躲避，只得硬生生地承受了这一击。她感觉胸口被重重地撞击了一下，随后涌上来的疼痛感令她呼吸急促。尽管她稳住了身体，但她的胸口仍然感到酸痛难忍。\n\n反过来，若叶向友奈的心脏发动攻击时，友奈感觉自己被一把利刃刺中了一样，一股刺痛直接穿透胸骨，传到了她的心脏，令她感到剧烈的胸闷和呼吸困难。她努力保持平衡，想要继续战斗，但身体已经开始不听使唤。\n\n在若叶向友奈的肝脏部位发动攻击时，友奈感觉到一股强烈的疼痛从腹部传来，令她瞬间失去了平衡。她弯曲着身体，咬紧牙关，但呼吸仍然十分困难，她感到自己仿佛要窒息一样。\n\n无论是被攻击到胸部还是腹部，两人都感受到了难以承受的疼痛，和让她们都无法忽视的破坏力。\n\n友奈和若叶又开始互相扑向对方，瞬间拉近距离。友奈快速挥起右拳，重重地砸向若叶的腹部，若叶吃痛，身体微微弯曲。同时，若叶右手猛然向前，向友奈的胸部狠狠一击，友奈退了一步，但仍然坚定地站稳。她张开双臂，向着若叶猛扑过去，双拳迅速挥向若叶的胸口和腹部，若叶无法闪躲，只能咬牙忍痛承受。\n\n若叶瞪大眼睛，狠狠地盯着友奈，试图找到她的弱点。她突然一个侧踢，狠狠踢向友奈的膝盖，友奈腿脚一软，但还是顽强地支撑着身体。友奈又挥起拳头，向着若叶的心脏狠狠一击，若叶捂住胸口，窒息的感觉袭来，但她没有退缩，而是狠狠回击，拳头重重地砸向友奈的胸口，友奈被猛烈的力量狠狠击中，痛苦地弯曲身体，几乎摔倒在地上。\n\n她猛地弹起身体，扑向若叶，双拳狂风般地砸向若叶的胸腹部，若叶躲闪不及，腹部被击中后，整个人倒飞出去，跌在地上，喉咙中发出一声闷哼。她试图爬起来，但身体已无力支撑，只能躺在地上，痛苦的喘息着。友奈则满身大汗，喘息声越来越大，她意识到自己已经快到极限了，但她仍然毫不畏惧地扑向若叶，继续这场激烈的比赛。\n\n两人在激烈的交手中，都意识到要攻击彼此身体的致命弱点。友奈和若叶都下定决心，要专注攻击对方的要害，以此来取得胜利。\n\n友奈猛然向前，右手飞快地挥出一拳，瞄准着若叶的心窝。若叶的反应也很迅速，她侧身闪避了友奈的拳头，同时用左手直接猛击友奈的肝脏。友奈顿时感到一阵剧痛袭来，呼吸也变得困难起来。\n\n不过友奈并没有放弃，她再次向若叶发起攻击，但这一次若叶的反应更加迅速，她侧身避开友奈的拳头，并用右手狠狠地击中友奈的胃部。友奈再次被痛苦折磨着，想要呕吐的感觉越来越强，口水从嘴角不受控制地流出，身体已经开始有些虚弱。\n\n这时，友奈再次发动攻击，这一次是左拳直接瞄准若叶的心脏部位。若叶的眼神闪过一丝惊恐，但她依旧不退缩，她的右手猛地击中了友奈的心脏部位。友奈感到自己的心脏似乎停了一拍，胸闷到了极点，同时感到一阵晕眩。\n[uploadedimage:15324760]\n若叶也感觉到自己的心脏受到了重击，但她并没有停下攻击。她向友奈发动了最后一次攻击，右手再次狠狠地砸在了友奈的胸前的心脏部位。友奈瞬间失去了意识，身体软倒在地。\n\n同时，若叶也感到自己的意识开始模糊，呼吸十分困难，她痛苦地倒在地上。整个场馆变得寂静。双方同时被KO。\n[uploadedimage:15324755][uploadedimage:15324756]",
//    "marker": null,
//    "illusts": [],
//    "images": {
//        "15324755": {
//            "novelImageId": "15324755",
//            "sl": "2",
//            "urls": {
//                "240mw": "https:\/\/i.pximg.net\/c\/240x480_80\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei749977690611_de1619a4c2b53a72542e404b856867ff_master1200.jpg",
//                "480mw": "https:\/\/i.pximg.net\/c\/480x960\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei749977690611_de1619a4c2b53a72542e404b856867ff_master1200.jpg",
//                "1200x1200": "https:\/\/i.pximg.net\/c\/1200x1200\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei749977690611_de1619a4c2b53a72542e404b856867ff_master1200.jpg",
//                "128x128": "https:\/\/i.pximg.net\/c\/128x128\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei749977690611_de1619a4c2b53a72542e404b856867ff_square1200.jpg",
//                "original": "https:\/\/i.pximg.net\/novel-cover-original\/img\/2023\/04\/18\/15\/55\/38\/tei749977690611_de1619a4c2b53a72542e404b856867ff.jpg"
//            }
//        },
//        "15324756": {
//            "novelImageId": "15324756",
//            "sl": "2",
//            "urls": {
//                "240mw": "https:\/\/i.pximg.net\/c\/240x480_80\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei884294845737_66bb29b8fe9b93af5f3697820d24cf94_master1200.jpg",
//                "480mw": "https:\/\/i.pximg.net\/c\/480x960\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei884294845737_66bb29b8fe9b93af5f3697820d24cf94_master1200.jpg",
//                "1200x1200": "https:\/\/i.pximg.net\/c\/1200x1200\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei884294845737_66bb29b8fe9b93af5f3697820d24cf94_master1200.jpg",
//                "128x128": "https:\/\/i.pximg.net\/c\/128x128\/novel-cover-master\/img\/2023\/04\/18\/15\/55\/38\/tei884294845737_66bb29b8fe9b93af5f3697820d24cf94_square1200.jpg",
//                "original": "https:\/\/i.pximg.net\/novel-cover-original\/img\/2023\/04\/18\/15\/55\/38\/tei884294845737_66bb29b8fe9b93af5f3697820d24cf94.jpg"
//            }
//        },
//        "15324760": {
//            "novelImageId": "15324760",
//            "sl": "6",
//            "urls": {
//                "240mw": "https:\/\/i.pximg.net\/c\/240x480_80\/novel-cover-master\/img\/2023\/04\/18\/15\/56\/26\/tei868077287841_9cfc192b47de20b2a065e5ed3dc92498_master1200.jpg",
//                "480mw": "https:\/\/i.pximg.net\/c\/480x960\/novel-cover-master\/img\/2023\/04\/18\/15\/56\/26\/tei868077287841_9cfc192b47de20b2a065e5ed3dc92498_master1200.jpg",
//                "1200x1200": "https:\/\/i.pximg.net\/c\/1200x1200\/novel-cover-master\/img\/2023\/04\/18\/15\/56\/26\/tei868077287841_9cfc192b47de20b2a065e5ed3dc92498_master1200.jpg",
//                "128x128": "https:\/\/i.pximg.net\/c\/128x128\/novel-cover-master\/img\/2023\/04\/18\/15\/56\/26\/tei868077287841_9cfc192b47de20b2a065e5ed3dc92498_square1200.jpg",
//                "original": "https:\/\/i.pximg.net\/novel-cover-original\/img\/2023\/04\/18\/15\/56\/26\/tei868077287841_9cfc192b47de20b2a065e5ed3dc92498.jpg"
//            }
//        }
//    },
//    "seriesNavigation": null,
//    "glossaryItems": [],
//    "replaceableItemIds": [],
//    "aiType": 2,
//    "isOriginal": false
//}
@Serializable
data class NovelData(
    val text: String,
    @SerialName("images")
    val _images: JsonElement
) {
    val images by lazy {
        check(_images is JsonObject) {
            "image is empty"
        }
        json.decodeFromJsonElement<Map<String, NovelImages>>(_images.jsonObject)
    }

    val data by lazy {
        createNovelData(text)
    }
}

enum class NovelImagesSize(val content: String) {
    N240Mw("240mw"),
    N480Mw("480mw"),
    N1200x1200("1200x1200"),
    N128x128("128x128"),
    NOriginal("original")
}
@Serializable
data class NovelImages(
    val urls: Map<String, String>
) {
    operator fun get(size: NovelImagesSize) = urls[size.content]?:NoSuchElementException("no size with $size")
}

@Suppress("all")
suspend fun PixivAccount.getNovelContent(novelId: Long): NovelData {
    val origin = client.get("webview/v2/novel") {
        parameter("id", novelId)
    }.bodyAsText().split("novel:")[1].split(",\n")[0].run { json.decodeFromString<NovelData>(this) }
    return origin
}