package top.kagg886.pixko.module.user

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import top.kagg886.pixko.PixivAccount
import top.kagg886.pixko.User
import top.kagg886.pixko.module.profile.CountryCode
import top.kagg886.pixko.module.profile.JapanAddress
import top.kagg886.pixko.module.profile.Job
import top.kagg886.pixko.module.user.Gender.*
import top.kagg886.pixko.module.user.UserPublicityOptions.*

/**
 * # 用户信息
 * @property user 用户账户信息
 * @property profile 用户个人信息
 * @property publicity 用户公开设置。当且仅当user代表自己时该字段认为有效
 * @property workspace 用户工作环境
 */
@Serializable
data class UserInfo(
    val user: User, val profile: UserProfile,

    //针对其他用户都是public
    @SerialName("profile_publicity") val publicity: UserPublicity, val workspace: UserWorkspace
)

/**
 * # 用户公开设置
 * @property public 公开
 * @property mypixiv 好P友可见
 * @property private 私密
 */
enum class UserPublicityOptions {
    public, mypixiv, private,
}

/**
 * # 用户工作环境
 * @property pc 电脑
 * @property monitor 显示器
 * @property tool 工具
 * @property scanner 扫描仪
 * @property tablet 平板
 * @property mouse 鼠标
 * @property printer 打印机
 * @property desktop 桌面
 * @property music 音乐
 * @property desk 桌布
 * @property chair 椅子
 * @property comment 评语
 * @property workspaceImageUrl 工作环境图片
 */
@Serializable
data class UserWorkspace(
    val pc: String,
    val monitor: String,
    val tool: String,
    val scanner: String,
    val tablet: String,
    val mouse: String,
    val printer: String,
    val desktop: String,
    val music: String,
    val desk: String,
    val chair: String,
    val comment: String,
    @SerialName("workspace_image_url") val workspaceImageUrl: String?
)

/**
 * # 用户公开设置
 * @property gender 性别
 * @property region 地区
 * @property birthDay 生日
 * @property birthYear 生日
 * @property job 工作
 * @property pawoo 是否开启pawoo
 */
@Serializable
data class UserPublicity(
    val gender: UserPublicityOptions,
    val region: UserPublicityOptions,
    @SerialName("birth_day") val birthDay: UserPublicityOptions,
    @SerialName("birth_year") val birthYear: UserPublicityOptions,
    val job: UserPublicityOptions,
    val pawoo: Boolean
)

/**
 * # 性别
 * @property MALE 男性
 * @property FEMALE 女性
 * @property UNKNOWN 未知
 * @property NOT_PUBLIC 未公开
 */
enum class Gender {
    MALE, FEMALE, UNKNOWN, NOT_PUBLIC
}

/**
 * # 用户个人信息
 * @property webpage 个人主页
 * @property _gender 性别字段，建议使用[gender]
 * @property _birth 生日字段，建议使用[birth]
 * @property region 地区
 * @property addressId 地址id，建议使用[address]
 * @property job 工作，建议使用[job]
 * @property totalFollowUsers 关注用户数
 * @property totalMyPixivUsers 好P友数
 * @property totalIllusts 作品数
 * @property totalManga 漫画数
 * @property totalNovels 小说数
 * @property totalIllustBookmarksPublic 收藏公开作品数
 * @property totalIllustSeries 作品系列数
 * @property totalNovelSeries 小说系列数
 * @property backgroundImageUrl 背景图片
 * @property twitterAccount twitter账号
 * @property twitterUrl twitter链接
 * @property pawooUrl pawoo链接
 * @property isPremium 是否为高级会员
 * @property isUsingCustomProfileImage 是否使用自定义背景图片
 **/
@Serializable
data class UserProfile(
    val webpage: String?,
    @SerialName("gender") private val _gender: String,

    @SerialName("birth") internal val _birth: String,
    val region: String,
    @SerialName("address_id") internal val addressId: Int,
    @SerialName("country_code") internal val countryCode: String,
    @SerialName("job_id") internal val jobId: Int,
    @SerialName("total_follow_users") val totalFollowUsers: Int,
    @SerialName("total_mypixiv_users") val totalMyPixivUsers: Int,
    @SerialName("total_illusts") val totalIllusts: Int,
    @SerialName("total_manga") val totalManga: Int,
    @SerialName("total_novels") val totalNovels: Int,
    @SerialName("total_illust_bookmarks_public") val totalIllustBookmarksPublic: Int,
    @SerialName("total_illust_series") val totalIllustSeries: Int,
    @SerialName("total_novel_series") val totalNovelSeries: Int,
    @SerialName("background_image_url") val backgroundImageUrl: String?,
    @SerialName("twitter_account") val twitterAccount: String,
    @SerialName("twitter_url") val twitterUrl: String?,
    @SerialName("pawoo_url") val pawooUrl: String?,
    @SerialName("is_premium") val isPremium: Boolean,
    @SerialName("is_using_custom_profile_image") val isUsingCustomProfileImage: Boolean
) {
    /**
     * 性别
     */
    val gender by lazy {
        when (_gender) {
            "male" -> MALE
            "female" -> FEMALE
            "unknown" -> UNKNOWN
            else -> NOT_PUBLIC
        }
    }

    /**
     * 生日
     */
    val birth by lazy {
        if (_birth.isEmpty()) {
            null
        } else {
            LocalDate.parse(_birth)
        }
    }

    /**
     * 地区
     * 0代表未设置
     */
    val address by lazy {
        JapanAddress.fromCode(addressId)!!
    }

    /**
     * 国家代号
     */
    val country by lazy {
        when (address) {
            JapanAddress.UN_SETTING -> CountryCode.UN_SETTING
            JapanAddress.INTERNATIONAL -> CountryCode.fromCode(countryCode)
            else -> CountryCode.JAPAN
        }
    }

    /**
     * 工作代号
     */
    val job by lazy {
        Job.fromCode(jobId)
    }
}


/**
 * # 获取用户信息
 * @param userId 用户id
 * @return [UserInfo] 用户信息
 *
 */
suspend fun PixivAccount.getUserInfo(userId: Int): UserInfo {
    return client.get("v1/user/detail?filter=for_android") {
        parameter("user_id", userId)
    }.body()
}
