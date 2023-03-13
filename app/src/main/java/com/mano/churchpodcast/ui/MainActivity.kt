package com.mano.churchpodcast.ui

import android.content.Intent
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.fscarapaica.player.extension.isStopped
import com.mano.churchpodcast.R
import com.mano.churchpodcast.databinding.ActivityMainBinding
import com.mano.churchpodcast.ext.getSelectedPosition
import com.mano.churchpodcast.ext.getSharedPreferences
import com.mano.churchpodcast.ext.isScrollEnable
import com.mano.churchpodcast.ext.navigateExoPlayer
import com.mano.churchpodcast.ui.adapter.LocationAdapter
import com.mano.churchpodcast.ui.dialog.LocationPopup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val activityViewModel: MainActivityViewModel by viewModels()
    private lateinit var locationAdapter: LocationAdapter
    private lateinit var popupView: Lazy<View>
    private lateinit var anchorView: Lazy<View>
    private var jesusURL = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityViewBinding = ActivityMainBinding.inflate(layoutInflater)
        setSupportActionBar(activityViewBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_discover, R.id.navigation_library
            )
        )

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_discover, R.id.navigation_home -> {
                    activityViewBinding.toolbar.isScrollEnable(true)
                    activityViewBinding.appBar.apply {
                        visibility = View.VISIBLE
                        setExpanded(true, true)
                    }
                }
                R.id.navigation_library -> {
                    activityViewBinding.appBar.visibility = View.INVISIBLE
                    activityViewBinding.toolbar.isScrollEnable(false)
                }
            }
        }

        setupActionBarWithNavController(navController, appBarConfiguration)
        activityViewBinding.navView.setupWithNavController(navController)

        locationAdapter = LocationAdapter(activityViewModel)
        activityViewModel.locationList.observe(this, { locationList ->
            val selectedPosition = baseContext.getSharedPreferences().getSelectedPosition()
            locationAdapter.submitList(locationList, selectedPosition)
        })

        activityViewModel.selectedLocation.observe(this, { location ->
            Glide.with(this)
                .load(location.image)
                .apply(LocationAdapter.REQUEST_OPTIONS)
                .transition(
                    DrawableTransitionOptions.withCrossFade(
                        DrawableCrossFadeFactory.Builder(500).setCrossFadeEnabled(true)
                            .build()
                    )
                )
                .into(activityViewBinding.currentLocation)
            jesusURL = location.jesus
        })

        activityViewBinding.currentLocation.setOnClickListener {
            onButtonShowPopupWindowClick()
        }

        popupView = lazy {
            layoutInflater.inflate(R.layout.dialog_location, null).apply {
                findViewById<RecyclerView>(R.id.rv_location).apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    adapter = locationAdapter
                }
            }
        }

        anchorView = lazy {
            findViewById(R.id.toolbar)
        }

        setContentView(activityViewBinding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_jesus -> {
            openWebIntent(jesusURL)
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun onButtonShowPopupWindowClick() {
        LocationPopup(popupView.value).show(anchorView.value)
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
        checkForMediaPlaying()
    }

    private fun checkForMediaPlaying() {
        if (MediaControllerCompat.getMediaController(this)?.playbackState?.isStopped == false) {
            navigateExoPlayer()
        }
    }

    private fun openWebIntent(url: String) {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(browserIntent)
    }

}
