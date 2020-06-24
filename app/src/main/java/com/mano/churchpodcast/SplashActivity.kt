package com.mano.churchpodcast

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.mano.churchpodcast.db.MediaItemRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private const val SPLASH_TIME_MS = 700L
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        GlobalScope.launch {
            MediaItemRepository(applicationContext).deleteAll()
        }

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
