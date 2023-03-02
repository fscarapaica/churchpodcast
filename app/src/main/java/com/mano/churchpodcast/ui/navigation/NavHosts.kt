package com.mano.churchpodcast.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mano.onboarding_presentation.navigation.Route
import com.mano.onboarding_presentation.welcome.WelcomeScreen

@Composable
fun OnboardingNavHost(
    navHostController: NavHostController,
    startDestination: String = Route.WELCOME
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination
    ) {
        composable(Route.WELCOME) {
            WelcomeScreen(onNavigate = navHostController::navigate)
        }
        composable(Route.SELECT_COUNTRY) {
            Column {

            }
        }
    }
}