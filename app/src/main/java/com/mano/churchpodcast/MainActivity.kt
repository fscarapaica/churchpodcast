package com.mano.churchpodcast

import android.content.Intent
import android.media.AudioManager
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.mano.churchpodcast.ui.player.PlayerActivity
import com.mano.churchpodcast.util.ACTIVITY_PLAYER_REQUEST_CODE
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        collapsed_player.setOnClickListener {
            val intent = Intent(baseContext, PlayerActivity::class.java)
            startActivityForResult(intent, ACTIVITY_PLAYER_REQUEST_CODE)
        }
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_library
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (ACTIVITY_PLAYER_REQUEST_CODE == requestCode) {
            //inflateCollapsedPLayer()
        }
    }

    private fun inflateCollapsedPLayer() {
        collapsed_player.getMediaController(this@MainActivity)
        collapsed_player.connectCollapsedPlayer()
    }

    override fun onStart() {
        super.onStart()
        inflateCollapsedPLayer()
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        collapsed_player.disconnectCollapsedPlayer()
        super.onStop()
    }

}
