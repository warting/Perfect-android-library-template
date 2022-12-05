plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.gladed.androidgitversion") version "0.4.14"
    id("se.premex.ownership") version "0.0.11"
}

ownership {
    validateOwnership = true
    generateGithubOwners = true
    generateMissingOwnershipFiles = true
    defaultOwnerForMissingOwnershipFiles = "@warting"
}

androidGitVersion {
    tagPattern = "^v[0-9]+.*"
}


val PUBLISH_GROUP_ID: String by extra("se.warting.perfect-android-library")
val PUBLISH_VERSION: String by extra(androidGitVersion.name().replace("v", ""))
val PUBLISH_ARTIFACT_ID by extra("core")

apply(from = "${rootProject.projectDir}/gradle/publish-module.gradle")
val composeVersion = "1.1.0-beta04"
android {
    compileSdk = 31

    defaultConfig {
        minSdk = 21
        targetSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = false
        compose = false
    }

    composeOptions {
        kotlinCompilerExtensionVersion = composeVersion
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
        freeCompilerArgs = listOfNotNull(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xskip-prerelease-check"
        )
    }

    lint {
        baseline = file("lint-baseline.xml")
        checkReleaseBuilds = true
        checkAllWarnings = true
        warningsAsErrors = true
        abortOnError = true
        disable.add("LintBaseline")
        disable.add("GradleDependency")
        checkDependencies = true
        checkGeneratedSources = false
        sarifOutput = file("../lint-results-lib.sarif")
    }
}

kotlin {
    // https://kotlinlang.org/docs/whatsnew14.html#explicit-api-mode-for-library-authors
    explicitApi()
}


dependencies {

    // implementation("androidx.compose.runtime:runtime:$composeVersion")
    // implementation("androidx.compose.ui:ui:$composeVersion")

    // val coroutineVersion = "1.5.2"
    // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutineVersion")

    // implementation("androidx.core:core-ktx:1.6.10")
    // implementation("androidx.appcompat:appcompat:1.3.1")
    // implementation("com.google.android.material:material:1.4.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.4")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.0")
}