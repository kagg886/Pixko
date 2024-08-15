import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("com.vanniktech.maven.publish") version "0.29.0"
}

group = "top.kagg886"
val APP_VERSION: String by project
check(APP_VERSION.startsWith("v")) {
    "APP_VERSION not supported, current is $APP_VERSION"
}
version = APP_VERSION.substring(1)

println("APP_VERSION: $version")

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-client-core:2.3.12")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.12")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.12")
    implementation("io.ktor:ktor-client-logging:2.3.12")
    implementation("io.ktor:ktor-client-cio:2.3.12")
    implementation("io.ktor:ktor-client-cio-jvm:2.3.12")

    implementation("org.jetbrains.kotlin:kotlin-reflect:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}

mavenPublishing {
    configure(
        KotlinJvm(
            // whether to publish a sources jar
            sourcesJar = true,
        )
    )
    publishToMavenCentral(SonatypeHost.S01)
    signAllPublications()
    coordinates(group.toString(), rootProject.name, version.toString())
    pom {
        name = "Pixko"
        description = "An api accesser for pixiv writed by kotlin "
        inceptionYear = "2024"
        url = "https://github.com/kagg886/Pixko/"
        licenses {
            license {
                name = "The Apache License, Version 2.0"
                url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                distribution = "https://www.apache.org/licenses/LICENSE-2.0.txt"
            }
        }
        developers {
            developer {
                id = "kagg886"
                name = "kagg886"
                url = "https://github.com/kagg886/"
            }
        }
        scm {
            url = "https://github.com/kagg886/Pixko/"
            connection = "scm:git:git://github.com/kagg886/Pixko.git"
            developerConnection = "scm:git:ssh://git@github.com/kagg886/Pixko.git"
        }
    }
}

fun getGitSha(): String {
    val out = Runtime.getRuntime().exec("git rev-parse --short HEAD").inputStream
    return out.readNBytes(7).decodeToString()
}