@file:OptIn(ExperimentalWasmDsl::class)
@file:Suppress("unused")

import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.SonatypeHost
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.testing.mocha.KotlinMocha

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.maven.publish)
}

val APP_VERSION = System.getenv("APP_VERSION") ?: project.findProperty("APP_VERSION") as? String ?: "unsetted."
check(APP_VERSION.startsWith("v")) {
    "APP_VERSION not supported, current is $APP_VERSION"
}

group = "top.kagg886"
version = APP_VERSION.substring(1)

println("APP_VERSION: $version")

kotlin {
    jvmToolchain(17)

    androidTarget { publishLibraryVariants("release") }
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    val mochaConfig: KotlinMocha.() -> Unit = {
        timeout = "10s"
    }

    js(IR) {
        moduleName = project.name
        browser {
            testTask {
                useMocha(mochaConfig)
            }
        }
        nodejs {
            testTask {
                useMocha(mochaConfig)
            }
        }
    }
    wasmJs {
        moduleName = project.name
        browser {
            testTask {
                useMocha(mochaConfig)
            }
        }
        nodejs {
            testTask {
                useMocha(mochaConfig)
            }
        }
    }

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.reflect)

            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)

            //shouldn't hard-encoding ktor engines, selected by user.
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.okio)
        }


        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
                implementation(libs.kotlinx.coroutines.test)
            }
        }

        jvmTest {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }


        appleTest  {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }

        val webTest by creating {
            dependsOn(commonTest.get())

            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
        wasmJsTest {
            dependsOn(webTest)
        }
        jsTest {
            dependsOn(webTest)
        }
    }

    // https://kotlinlang.org/docs/native-objc-interop.html#export-of-kdoc-comments-to-generated-objective-c-headers
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        compilations["main"].compileTaskProvider.configure {
            compilerOptions {
                freeCompilerArgs.add("-Xexport-kdoc")
            }
        }
    }
    // https://youtrack.jetbrains.com/issue/KT-61573
    targets.configureEach {
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    freeCompilerArgs.add("-Xexpect-actual-classes")
                }
            }
        }
    }
}


android {
    namespace = "top.kagg886.pixko"
    compileSdk = 35

    defaultConfig {
        minSdk = 21
    }
}
dependencies {
    implementation("io.ktor:ktor-client-okhttp-jvm:3.0.3")
}


mavenPublishing {
    configure(
        KotlinMultiplatform(
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

            developer {
                id = "Akari"
                name = "Akari"
                url = "https://github.com/iAkariAk/"
            }
        }
        scm {
            url = "https://github.com/kagg886/Pixko/"
            connection = "scm:git:git://github.com/kagg886/Pixko.git"
            developerConnection = "scm:git:ssh://git@github.com/kagg886/Pixko.git"
        }
    }
}
