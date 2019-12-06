package com.mano.hillsongpodcast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

private const val SPLASH_TIME_MS = 1000L
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            gotoMainActivity()
        }, SPLASH_TIME_MS)
    }

    private fun gotoMainActivity() {
        Intent(applicationContext, MainActivity::class.java).run {
            startActivity(this)
        }
        finish()
    }
}
