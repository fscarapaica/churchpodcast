apply(from = "$rootDir/base-module.gradle")

plugins {
    id("com.android.library")
}

android {
    namespace = "com.mano.onboarding_domain"
}

dependencies {
    implementation(project(Modules.core))

}