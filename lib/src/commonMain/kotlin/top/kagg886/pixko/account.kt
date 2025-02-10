package top.kagg886.pixko

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import top.kagg886.pixko.TokenType.ACCESS
import top.kagg886.pixko.TokenType.REFRESH
import top.kagg886.pixko.internal.TokenAutoRefreshPluginV2
import kotlin.collections.set

/**
 * # Token类型
 * @property ACCESS
 * @property REFRESH
 */
enum class TokenType {
    ACCESS, REFRESH
}

/**
 * # Token存储器
 * @see InMemoryTokenStorage
 */
interface TokenStorage {
    fun getToken(type: TokenType): String?
    fun setToken(type: TokenType, token: String)
}

/**
 * # 将token存入内存的Token存储器
 */
class InMemoryTokenStorage : TokenStorage {
    private val map = mutableMapOf<TokenType, String>()
    override fun getToken(type: TokenType): String? = map[type]

    override fun setToken(type: TokenType, token: String) {
        map[type] = token
    }
}

/**
 * # Pixiv账号配置
 * @see PixivAccount
 * @property storage token存储器
 * @property logger 日志工具
 * @property language 语言配置，置null则为日文。
 */
class PixivAccountConfig {
    var storage: TokenStorage = InMemoryTokenStorage()
    var logger: Logger = Logger.EMPTY
    var language: String? = "zh-CN"
    var engine: CIOEngineConfig.() -> Unit = {}
}

/**
 * # PixivAPP Client
 * 内部仅包含程序的核心部分，api定义在[top.kagg886.pixko.module]中
 *
 * @see PixivAccountFactory
 */
class PixivAccount internal constructor(private val config: PixivAccountConfig) :
    AutoCloseable {

    internal val client = HttpClient(CIO) {
        engine {
            config.engine(this)
        }

        install(ContentNegotiation) {
            json(top.kagg886.pixko.internal.json)
        }


        install(TokenAutoRefreshPluginV2) {
            storage = config.storage
        }

        install(Logging) {
            logger = config.logger
            level = LogLevel.ALL
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30000
            socketTimeoutMillis = 30000
            connectTimeoutMillis = 30000
        }

        defaultRequest {
            url("https://app-api.pixiv.net/")
            header("Accept-Language", config.language)
            header("Referer","https://app-api.pixiv.net/")
        }
    }

    override fun close() {
        client.close()
    }
}