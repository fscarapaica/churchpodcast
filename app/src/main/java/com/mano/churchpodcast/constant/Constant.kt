package com.mano.churchpodcast.constant

import androidx.navigation.navOptions
import com.mano.churchpodcast.R

internal const val YOUTUBE_API_KEY = "your_youtube_key"

internal val defaultNavigationOptions = navOptions {
    anim { enter = R.anim.slide_up
        exit = R.anim.slide_right
        popExit = R.anim.slide_right
    }
    launchSingleTop = true
}