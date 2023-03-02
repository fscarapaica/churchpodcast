package com.mano.churchpodcast.ui.navigation

import androidx.navigation.NavHostController
import com.mano.core.util.UiEvent

fun NavHostController.navigate(event: UiEvent.Navigate) {
    this.navigate(event.route)
}