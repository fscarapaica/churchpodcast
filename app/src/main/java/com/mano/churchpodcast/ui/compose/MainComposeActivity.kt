package com.mano.churchpodcast.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.mano.churchpodcast.ui.navigation.OnboardingNavHost
import com.mano.onboarding_presentation.welcome.WelcomeScreen

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChurchPodcastTheme {
                val navHostController = rememberNavController()
                OnboardingNavHost(navHostController = navHostController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ChurchPodcastTheme {
        WelcomeScreen {

        }
    }
}