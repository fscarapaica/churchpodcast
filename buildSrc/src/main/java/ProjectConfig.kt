import org.gradle.api.JavaVersion

object ProjectConfig {

    @JvmField val javaVersion = JavaVersion.VERSION_11

    const val appId = "com.mano.churchpodcast"
    const val compileSdk = 33
    const val minSdk = 21
    const val targetSdk = 33
    const val versionCode = 5
    const val versionName = "2.0.1"

    object PlayerOld {
        @Deprecated("Deprecated project use player_presentation and player_domain")
        const val libraryId = "com.fscarapaica.player"
    }

}