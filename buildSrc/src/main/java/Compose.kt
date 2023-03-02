object Compose {

    /**
     * https://developer.android.com/jetpack/compose/setup#bom-version-mapping
     */
    private const val composeBOMVersion = "2023.01.00"
    const val composeBOM = "androidx.compose:compose-bom:$composeBOMVersion"

    const val composeMaterial3 = "androidx.compose.material3:material3"

    const val composeToolingPreview = "androidx.compose.ui:ui-tooling-preview"
    const val composeTooling = "androidx.compose.ui:ui-tooling"

    const val composeTestJunit4 = "androidx.compose.ui:ui-test-junit4"
    const val composeTestManifest = "androidx.compose.ui:ui-test-manifest"

    const val composeLiveData = "androidx.compose.runtime:runtime-livedata"

    private const val composeNavigationVersion = "2.5.3"
    const val composeNavigation = "androidx.navigation:navigation-compose:$composeNavigationVersion"

    private const val composeActivityVersion = "1.6.1"
    const val composeActivity = "androidx.activity:activity-compose:$composeActivityVersion"

    private const val composeLifecycleViewModelVersion = "2.5.1"
    const val composeLifecycleViewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:$composeLifecycleViewModelVersion"

}