package com.mano.churchpodcast.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.gms.common.GoogleApiAvailability
import com.mano.churchpodcast.ui.MainActivity

class RouterActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1000
    }

    private val routerActivityViewModel: RouterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        routerActivityViewModel.launchMainActivity.observe(this) { launchMainActivity ->
            if (launchMainActivity) {
                gotoMainActivity()
            }
        }

        routerActivityViewModel.isGooglePlayServiceUpdated.observe(this) { isUpdated ->
            if (!isUpdated) {
                acquireGooglePlayServices()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            routerActivityViewModel.launchMainActivity.value == false
        }
    }

    private fun gotoMainActivity() {
        Intent(applicationContext, MainActivity::class.java).run {
            startActivity(this)
        }
        finish()
    }

    private fun acquireGooglePlayServices() {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode)
        }
    }

    private fun showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode: Int) {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val dialog = apiAvailability.getErrorDialog(
            this@RouterActivity,
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        )
        dialog?.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> routerActivityViewModel.googlePlayServiceUpdate()
        }
    }

}
