plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = ProjectConfig.appId
    compileSdk = ProjectConfig.compileSdk

    defaultConfig {
        applicationId = ProjectConfig.appId
        minSdk = ProjectConfig.minSdk
        targetSdk = ProjectConfig.targetSdk
        versionCode = ProjectConfig.versionCode
        versionName = ProjectConfig.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.2"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = ProjectConfig.javaVersion
        targetCompatibility = ProjectConfig.javaVersion
    }
    kotlinOptions {
        jvmTarget = ProjectConfig.javaVersion.toString()
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
        }
    }
}

dependencies {
    implementation(project(Modules.player))
    implementation(project(Modules.core))
    implementation(project(Modules.core_compose))
    implementation(project(Modules.onboarding_domain))
    implementation(project(Modules.onboarding_presentation))

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(files("libs/${Player.youtubePlayerLib}"))

    implementation(AndroidX.appCompat)
    implementation(AndroidX.coreKtx)
    implementation(AndroidX.splashSreen)

    implementation(Coroutines.core)

    implementation(Google.material)
    implementation(AndroidX.swipeRefreshLayout)
    implementation(AndroidX.constrainLayout)
    implementation(AndroidX.vectorDrawable)
    implementation(AndroidX.navigationFragmentKtx)
    implementation(AndroidX.navigationUIKtx)

    implementation(AndroidX.room)
    implementation(AndroidX.roomKtx)
    kapt(AndroidX.roomKapt)

    implementation(AndroidX.legacySupport)

    implementation(AndroidX.lifecycleLivedataKtx)
    implementation(AndroidX.lifecycleViewModelKtx)
    implementation(AndroidX.lifecycleExtension)

    // Firebase BOM and dependencies
    implementation(platform(Firebase.bom))
    implementation(Firebase.configKtx)
    implementation(Firebase.analyticsKtx)
    implementation(Firebase.fireStore)
    implementation(Firebase.authKtx)

    //Gson
    implementation(Gson.gson)

    // Web scrapping
    implementation(Jsoup.jsoup)

    // Image fetching library
    implementation(Glide.glide)
    kapt(Glide.glideCompiler)

    // Compose
    val composeBom = platform(Compose.composeBOM)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose dependencies
    implementation(Compose.composeMaterial3)

    // Android Studio Preview support
    implementation(Compose.composeToolingPreview)
    debugImplementation(Compose.composeTooling)

    // UI Tests
    androidTestImplementation(Compose.composeTestJunit4)
    debugImplementation(Compose.composeTestManifest)

    // Optional - Integration with activities
    implementation(Compose.composeActivity)
    // Optional - Integration with ViewModels
    implementation(Compose.composeLifecycleViewModel)
    // Optional - Integration with LiveData
    implementation(Compose.composeLiveData)

    testImplementation(Test.junit)
    androidTestImplementation(Test.junitAndroid)
    androidTestImplementation(Test.junitAndroidEspresso)
}
