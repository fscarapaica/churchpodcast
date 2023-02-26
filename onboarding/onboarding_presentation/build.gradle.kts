apply(from = "$rootDir/compose-lib-module.gradle")

plugins {
    id("com.android.library")
}

android {
    namespace = "com.mano.onboarding_presentation"
}

dependencies {
    implementation(project(Modules.core))

    implementation(AndroidX.coreKtx)
    implementation(AndroidX.appCompat)
    implementation(Google.material)
}