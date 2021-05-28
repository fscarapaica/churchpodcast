package com.mano.churchpodcast.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.GoogleApiAvailability
import com.mano.churchpodcast.ui.MainActivity
import com.mano.churchpodcast.R

class SplashActivity : AppCompatActivity() {

    companion object {
        const val REQUEST_GOOGLE_PLAY_SERVICES = 1000
    }

    private val splashActivityViewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashActivityViewModel.launchMainActivity.observe(this) { launchMainActivity ->
            if (launchMainActivity) {
                gotoMainActivity()
            }
        }

        splashActivityViewModel.isGooglePlayServiceUpdated.observe(this) { isUpdated ->
            if (!isUpdated) {
                acquireGooglePlayServices()
            }
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
            this@SplashActivity,
            connectionStatusCode,
            REQUEST_GOOGLE_PLAY_SERVICES
        )
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_GOOGLE_PLAY_SERVICES -> splashActivityViewModel.googlePlayServiceUpdate()
        }
    }

}
