package top.kagg886.pixko

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import top.kagg886.pixko.internal.TokenAutoRefreshPlugin
import kotlin.collections.mutableMapOf
import kotlin.collections.set

enum class TokenType {
    ACCESS, REFRESH
}

interface TokenStorage {
    fun getToken(type: TokenType): String?
    fun setToken(type: TokenType, token: String)
}

class InMemoryTokenStorage : TokenStorage {
    private val map = mutableMapOf<TokenType, String>()
    override fun getToken(type: TokenType): String? = map[type]

    override fun setToken(type: TokenType, token: String) {
        map[type] = token
    }
}

class PixivAccountConfig {

    data class LoggerProprieties(
        val level: LogLevel,
        val logger: Logger
    )

    var storage: TokenStorage = InMemoryTokenStorage()
    var logger: LoggerProprieties = LoggerProprieties(LogLevel.NONE, Logger.DEFAULT)
}

class PixivAccount internal constructor(private val config: PixivAccountConfig) :
    AutoCloseable {

    internal val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(top.kagg886.pixko.internal.json)
        }

        install(TokenAutoRefreshPlugin) {
            tokenStorage = config.storage
        }

        install(Logging) {
            logger = config.logger.logger
            level = config.logger.level
        }

        install(Logging) {
            level = LogLevel.BODY
        }

        defaultRequest {
            url("https://app-api.pixiv.net/")
        }
    }

    override fun close() {
        client.close()
    }
}