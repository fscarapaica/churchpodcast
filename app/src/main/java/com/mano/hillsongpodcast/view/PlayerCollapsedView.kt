package com.mano.hillsongpodcast.view

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.fscarapaica.player.extension.displayIconUri
import com.fscarapaica.player.extension.displayTitle
import com.fscarapaica.player.extension.isStopped
import com.fscarapaica.player.service.MediaPlaybackService
import com.fscarapaica.player.view.MediaSeekBar
import com.mano.hillsongpodcast.R
import kotlinx.android.synthetic.main.component_player_collapsed.view.*

class PlayerCollapsedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    private var mediaBrowser: MediaBrowserCompat? = null

    private lateinit var mediaSeekBar: MediaSeekBar

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.component_player_collapsed, this, true)
        orientation = VERTICAL
        visibility = VISIBLE
        setBackgroundResource(R.drawable.background_round_outline)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        mediaSeekBar = exo_progress
    }

    fun getMediaController(activity: Activity) {
        mediaBrowser = MediaBrowserCompat(activity,
            ComponentName(activity, MediaPlaybackService::class.java),
            connectionCallBacks(activity),
            null
        )
    }

    fun connectCollapsedPlayer() {
        visibility = VISIBLE
        mediaBrowser?.apply {
            connect()
        }
    }

    fun disconnectCollapsedPlayer(mediaControllerCompat: MediaControllerCompat?) {
        visibility = GONE
        mediaControllerCompat?.unregisterCallback(controllerCallback)
        mediaSeekBar.disconnectController()
        mediaBrowser?.apply {
            disconnect()
        }
    }

    private fun connectionCallBacks(activity: Activity) = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser?.sessionToken?.also { token ->
                val mediaController = MediaControllerCompat(activity, token)
                mediaController.registerCallback(controllerCallback)
                MediaControllerCompat.setMediaController(activity, mediaController)

                mediaSeekBar.setMediaController(mediaController)
                controllerCallback.onMetadataChanged(mediaController.metadata)
                controllerCallback.onPlaybackStateChanged(mediaController.playbackState)
            }
        }

        override fun onConnectionSuspended() {
            // TODO: Disable the user UI since the service has crashed
        }

        override fun onConnectionFailed() {
            // TODO: the service has refused our connection
        }
    }

    private var controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            metadata?.let {
                Glide.with(context)
                    .load(it.displayIconUri)
                    .into(iv_media_image)

                exo_title.text = it.displayTitle
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            state?.let {
                exo_play_pause.isSelected = it.state == PlaybackStateCompat.STATE_PLAYING
                visibility = if (it.isStopped) GONE else VISIBLE
            }
        }

    }

}
