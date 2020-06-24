package com.mano.churchpodcast.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.fscarapaica.player.extension.displayIconUri
import com.fscarapaica.player.extension.displayTitle
import com.fscarapaica.player.extension.isStopped
import com.fscarapaica.player.service.MediaPlaybackService
import com.fscarapaica.player.view.MediaSeekBar
import com.mano.churchpodcast.R
import kotlinx.android.synthetic.main.component_player_collapsed.view.*

class PlayerCollapsedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {

    private var mediaBrowser: MediaBrowserCompat? = null
    private var mediaController: MediaControllerCompat? = null
    private lateinit var mediaSeekBar: MediaSeekBar

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.component_player_collapsed, this, true)
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
        mediaBrowser?.apply {
            connect()
        }
    }

    fun disconnectCollapsedPlayer() {
        visibility = GONE
        mediaController?.unregisterCallback(controllerCallback)
        mediaSeekBar.disconnectController()
        mediaBrowser?.apply {
            disconnect()
        }
    }

    private fun connectionCallBacks(activity: Activity) = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser?.sessionToken?.also { token ->
                mediaController = MediaControllerCompat(activity, token)
                mediaController?.registerCallback(controllerCallback)
                MediaControllerCompat.setMediaController(activity, mediaController)

                mediaSeekBar.setMediaController(mediaController)
                controllerCallback.onMetadataChanged(mediaController!!.metadata)
                controllerCallback.onPlaybackStateChanged(mediaController!!.playbackState)

                exo_play_pause.setOnClickListener { view ->
                    if (mediaController?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING) {
                        mediaController?.transportControls?.pause()
                        view.isSelected = false
                    } else {
                        mediaController?.transportControls?.play()
                        view.isSelected = true
                    }
                }

                exo_stop.setOnClickListener {
                    mediaController?.transportControls?.stop()
                }
            }
        }

        // Disable the user UI since the service has crashed
        override fun onConnectionSuspended() {
            visible(false)
        }

        // the service has refused our connection
        override fun onConnectionFailed() {
            visible(false)
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
                visible(!it.isStopped)
            }
        }

    }

    private fun visible(isVisible: Boolean) {
        visibility = if (isVisible) VISIBLE else GONE
    }

    // TODO: REVISIT THE ON SWIPE TOUCH LISTENER
    @SuppressLint("ClickableViewAccessibility")
    fun setOnTouchListener() {
        setOnTouchListener(object : OnSwipeTouchListener(context, this) {

            override fun onSwipeRight() {
                mediaController?.transportControls?.stop()
            }

            override fun onSwipeLeft() {
                super.onSwipeLeft()
            }
        })
    }

}
