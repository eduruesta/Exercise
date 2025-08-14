import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.hotReload)
    alias(libs.plugins.kotlinx.serialization)
}

kotlin {
    jvmToolchain(22)

    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            // Koin
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            // Ktor
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            // Coroutines
            implementation(libs.kotlinx.coroutines.core)

            // Serialization
            implementation(libs.kotlinx.serialization.json)

            // ViewModel
            implementation(libs.lifecycle.viewmodel)

            implementation("io.coil-kt.coil3:coil-compose:3.3.0")

            implementation(libs.navigation.compose)

            // RevenueCat (temporarily disabled due to library issues)
            implementation(libs.purchases.core)
            implementation(libs.purchases.ui)
            implementation("io.github.khubaibkhan4:mediaplayer-kmp:2.0.9")

        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)

            // Koin Android
            implementation(libs.koin.android)

            // Ktor Android
            implementation(libs.ktor.client.android)

            // Coroutines Android
            implementation(libs.kotlinx.coroutines.android)

            // Coil Network for Android
            implementation(libs.coil3.coil.network.ktor)
            
            // Video Player for Android
            implementation(libs.media3.exoplayer)
            implementation(libs.media3.ui)
        }


        iosMain.dependencies {
            // Ktor iOS
            implementation(libs.ktor.client.darwin)
            implementation(libs.coil3.coil.network.ktor)
        }
        
        // Configure iOS targets for RevenueCat
        targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
            if (name.startsWith("ios")) {
                compilations["main"].compileTaskProvider.configure {
                    compilerOptions {
                        freeCompilerArgs.add("-Xopt-in=kotlinx.cinterop.ExperimentalForeignApi")
                    }
                }
            }
        }

    }
}

android {
    namespace = "org.dev.exercises"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        targetSdk = 35

        applicationId = "org.dev.exercises.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}


