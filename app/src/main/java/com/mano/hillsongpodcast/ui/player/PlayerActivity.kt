package com.mano.hillsongpodcast.ui.player

import android.content.ComponentName
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.fscarapaica.player.extension.displayDescription
import com.fscarapaica.player.extension.displayIconUri
import com.fscarapaica.player.extension.displayTitle
import com.fscarapaica.player.extension.putMediaMetadataCompat
import com.fscarapaica.player.service.MediaPlaybackService
import com.mano.hillsongpodcast.R
import com.mano.hillsongpodcast.model.MediaItem
import com.fscarapaica.player.view.MediaSeekBar
import com.mano.hillsongpodcast.util.getJsonExtra
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.player_control_view.*

class PlayerActivity : AppCompatActivity() {

    private lateinit var mediaBrowser: MediaBrowserCompat

    private lateinit var mediaSeekBar: MediaSeekBar
    private lateinit var playPauseButton: ImageButton

    private var mediaItem: MediaItem? = null

    private val connectionCallBacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                val mediaController = MediaControllerCompat(this@PlayerActivity, token)
                mediaController.registerCallback(controllerCallback)
                MediaControllerCompat.setMediaController(this@PlayerActivity, mediaController)
                mediaSeekBar.setMediaController(mediaController)
                controllerCallback.onMetadataChanged(mediaController.metadata)
                controllerCallback.onPlaybackStateChanged(mediaController.playbackState)
            }
            val bundle = Bundle()
            mediaItem?.let { mediaItem ->
                MediaMetadataCompat.Builder()
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, mediaItem.title)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION, mediaItem.description)
                    .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, mediaItem.image)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaItem.id.toString())
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, mediaItem.link)
                    .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, mediaItem.author)
                    .putString(MediaMetadataCompat.METADATA_KEY_DATE, mediaItem.date)
                    .build()
            }?.also { mediaMetadataCompat ->
                bundle.putMediaMetadataCompat(mediaMetadataCompat)
            }?.also {
                MediaControllerCompat.getMediaController(this@PlayerActivity)
                    .transportControls
                    .prepareFromUri(Uri.parse(mediaItem?.mediaLink), bundle)
            }
            // TODO: Since when this is call the UI is already build, connects the media controller with the UI
        }

        override fun onConnectionSuspended() {
            // TODO: Disable the user UI since the service has crashed
        }

        override fun onConnectionFailed() {
            // TODO: the service has refused our connection
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        mediaItem = intent.getJsonExtra(MediaItem::class.java)

        mediaBrowser = MediaBrowserCompat(this,
            ComponentName(this, MediaPlaybackService::class.java),
            connectionCallBacks,
            null
        )

        // inflating views
        mediaSeekBar = exo_progress
        playPauseButton = exo_play_pause
        mediaSeekBar.timeEventListener = object : MediaSeekBar.TimeEventListener {
            override fun onTimeUpdate(timeMs: Int) {
                exo_position.text = formatElapsedTimeMS(timeMs)
            }

            override fun onDurationUpdate(durationMs: Int) {
                exo_duration.text = formatElapsedTimeMS(durationMs)
            }
        }

        //Click listener
        ControlsClickListener().apply {
            playPauseButton.setOnClickListener(this)
            exo_ffwd.setOnClickListener(this)
            exo_rew.setOnClickListener(this)
        }
        iv_minimize.setOnClickListener {
            finish()
        }
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
        mediaSeekBar.disconnectController()
        mediaBrowser.disconnect()
    }

    private var controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            metadata?.let { metadata ->
                Glide.with(this@PlayerActivity)
                    .load(metadata.displayIconUri)
                    .into(iv_media_image)

                val descriptor = metadata.description

                exo_title.text = metadata.displayTitle
                exo_description.text = "${metadata.bundle.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)} · " +
                        "${metadata.bundle.getString(MediaMetadataCompat.METADATA_KEY_DATE)} · " +
                        "${metadata.displayDescription} "
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playPauseButton.isSelected = state?.state == PlaybackStateCompat.STATE_PLAYING
            Log.d("PlayerActivity", "$state ")
        }

    }

    private inner class ControlsClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            MediaControllerCompat
                .getMediaController(this@PlayerActivity)?.let { mediaController ->
                    when (view.id) {
                        R.id.exo_ffwd -> mediaController.transportControls.fastForward()
                        R.id.exo_rew ->  mediaController.transportControls.rewind()
                        R.id.exo_play_pause -> {
                            if (mediaController.playbackState.state == PlaybackStateCompat.STATE_PLAYING) {
                                mediaController.transportControls.pause()
                                view.isSelected = false
                            } else {
                                mediaController.transportControls.play()
                                view.isSelected = true
                            }
                        }
                    }
            }
        }
    }

    fun formatElapsedTimeMS(timeMS: Int): String = DateUtils.formatElapsedTime(timeMS/MS_BASE)

}

private const val MS_BASE = 1000L