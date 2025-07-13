package top.kagg886.pixko.internal

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal val json = Json {
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        contextual(Instant::class, InstantAsTimeStampSerializer)
    }
}
