apply(from = "$rootDir/base-lib-module.gradle")

plugins {
    id("com.android.library")
}

android {
    namespace = "com.mano.onboarding_domain"
}

dependencies {
    implementation(project(Modules.core))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Google.material)
}