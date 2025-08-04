import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.jetbrains.kotlinx.serialization.json)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
            implementation("com.russhwolf:multiplatform-settings:1.1.1")
            implementation("com.russhwolf:multiplatform-settings-serialization:1.1.1")
            implementation("com.russhwolf:multiplatform-settings-coroutines:1.1.1")
        }
    }
}


compose.desktop {
    application {
        mainClass = "com.vompom.typewriter.mingkwai_typewriter.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.vompom.typewriter.mingkwai_typewriter"
            packageVersion = "1.0.0"
            nativeDistributions {
                appResourcesRootDir = rootDir.resolve("myAssets/")
            }

        }
    }
}

