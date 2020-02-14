package com.mano.hillsongpodcast.ui.player

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaControllerCompat
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.mano.hillsongpodcast.R
import com.mano.hillsongpodcast.model.MediaItem
import com.mano.hillsongpodcast.ui.player.service.MediaPlaybackService
import com.mano.hillsongpodcast.ui.player.service.MediaPlaybackService2
import com.mano.hillsongpodcast.util.getJsonExtra
import kotlinx.android.synthetic.main.activity_player.*


class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaBrowser: MediaBrowserCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        /*
        // TODO: Set an extra key for the media item
        val mediaitem = intent.getJsonExtra(MediaItem::class.java)
        Log.d("PlayerActivity", "lala $mediaitem ")

        val exoPlayer = SimpleExoPlayer.Builder(baseContext).build().apply {
            playWhenReady = true // Auto play when ready
        }

        player_control_view.apply {
            player = exoPlayer
            showTimeoutMs = 0 // Always show the controllers
        }

        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(
            baseContext,
            Util.getUserAgent(baseContext, "HillsongPodcast")
        )

        mediaitem?.link?.let {
            val audioSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(Uri.parse("https://d9nqqwcssctr8.cloudfront.net/wp-content/uploads/2019/12/05015719/PODCAST-CORAZON-POR-LA-CASA-2019.mp3"))
            exoPlayer.prepare(audioSource)
        }
         */

        mediaBrowser = MediaBrowserCompat(this,
            ComponentName(this, MediaPlaybackService::class.java),
            connectionCallBacks,
            null
        )
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        volumeControlStream = AudioManager.STREAM_MUSIC
    }

    override fun onStop() {
        super.onStop()
        MediaControllerCompat.getMediaController(this)?.unregisterCallback(controllerCallback)
        mediaBrowser.disconnect()
    }


}

class DescriptorAdapter(private val application: Application) : PlayerNotificationManager.MediaDescriptionAdapter {
    override fun createCurrentContentIntent(player: Player): PendingIntent? {
        return PendingIntent.getActivity(application,
            0,
            Intent(application, PlayerActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT)
    }

    override fun getCurrentContentText(player: Player): String? {
        return null
    }

    override fun getCurrentContentTitle(player: Player): String {
        return "getCurrentContentTitle"
    }

    override fun getCurrentLargeIcon(
        player: Player,
        callback: PlayerNotificationManager.BitmapCallback
    ): Bitmap? {
        return BitmapFactory.decodeResource(application.resources, R.drawable.hillson_logo)
    }

}
