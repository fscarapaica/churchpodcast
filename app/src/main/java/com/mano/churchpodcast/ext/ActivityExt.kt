package com.mano.churchpodcast.ext

import android.app.Activity
import android.os.Bundle
import androidx.navigation.findNavController
import com.mano.churchpodcast.R
import com.mano.churchpodcast.constant.defaultNavigationOptions

fun Activity.navigateExoPlayer() {
    findNavController(R.id.nav_player_fragment).apply {
        navigateUp()
        navigate(R.id.navigation_exo_player, Bundle.EMPTY, defaultNavigationOptions)
    }
}