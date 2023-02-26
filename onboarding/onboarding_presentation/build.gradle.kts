apply(from = "$rootDir/base-compose-module.gradle")

plugins {
    id("com.android.library")
}

android {
    namespace = "com.mano.onboarding_presentation"
}

dependencies {
    implementation(project(Modules.core))
    implementation(project(Modules.core_compose))

    implementation(AndroidX.coreKtx)
}