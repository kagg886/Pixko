package top.kagg886.pixko.module.ugoira

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.ImageUrls
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.module.illust.Illust

//{
//    "ugoira_metadata": {
//        "zip_urls": {
//            "medium": "https://i.pximg.net/img-zip-ugoira/img/2025/01/22/22/57/49/126473222_ugoira600x600.zip"
//        },
//        "frames": [
//            {
//                "file": "000000.jpg",
//                "delay": 60
//            },
//            {
//                "file": "000001.jpg",
//                "delay": 70
//            },
//            {
//                "file": "000002.jpg",
//                "delay": 70
//            },
//            {
//                "file": "000003.jpg",
//                "delay": 60
//            },
//            {
//                "file": "000004.jpg",
//                "delay": 70
//            },
//            {
//                "file": "000005.jpg",
//                "delay": 70
//            }
//        ]
//    }
//}

/**
 * # 动图元数据
 *
 * @property url zip文件下载地址
 * @property frames 动图帧元数据
 */
@Serializable
data class UgoiraMetadata(
    @SerialName("zip_urls")
    val url: ImageUrls,
    val frames: List<UgoiraFrame>
)

/**
 * # 动图帧控制信息
 *
 * @property file 动图帧文件名
 * @property delay 动图帧延迟时间，单位为毫秒
 */
@Serializable
data class UgoiraFrame(
    val file: String,
    val delay: Int
)

/**
 * # 获取动图元数据
 *
 * @param illust 插画id
 * @return 动图元数据
 * @throws IllegalStateException 如果插画不是动图
 */
suspend fun PixivAccount.getUgoiraMetadata(illust: Illust): UgoiraMetadata {
    check(illust.isUgoira) {
        "illust is not ugoira."
    }

    val resp = client.get("v1/ugoira/metadata") {
        parameter("illust_id", illust.id)
    }

    @Serializable
    data class UgoiraMetadataReturn(
        @SerialName("ugoira_metadata") val data: UgoiraMetadata
    )

    return resp.body<UgoiraMetadataReturn>().data
}
