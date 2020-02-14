package com.mano.hillsongpodcast.ui.player.service

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes

private const val LOG_TAG = "MediaPlaybackService"
private const val MY_MEDIA_ROOT_ID = "media_root_id"
private const val MY_EMPTY_MEDIA_ROOT_ID = "empty_root_id"

@SuppressLint("Registered")
class MediaPlaybackService : MediaBrowserServiceCompat() {

    private lateinit var mediaSession: MediaSessionCompat
    private lateinit var stateBuilder: PlaybackStateCompat.Builder

    private val uAmpAudioAttributes = AudioAttributes.Builder()
        .setContentType(C.CONTENT_TYPE_MUSIC)
        .setUsage(C.USAGE_MEDIA)
        .build()

    /**
     * Configure ExoPlayer to handle audio focus for us.
     * See [Player.AudioComponent.setAudioAttributes] for details.
     *
     */
    private val exoPlayer: ExoPlayer by lazy {
        SimpleExoPlayer.Builder(baseContext).build().apply {
            playWhenReady = true
            setAudioAttributes(uAmpAudioAttributes, true)
        }
    }

    override fun onCreate() {
        super.onCreate()

        // Create a MediaSessionCompat
        mediaSession = MediaSessionCompat(baseContext, LOG_TAG).apply {
            // Enable callbacks from MediaButtons and TransportControls
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY
                            or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())
            setCallback(sessionCallback)
        }
        // Set the session's token so that client activities can communicate with it.
        sessionToken = mediaSession.sessionToken
    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        if (MY_EMPTY_MEDIA_ROOT_ID == parentId) {
            result.sendResult(null)
            return
        }
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        return BrowserRoot(MY_MEDIA_ROOT_ID, null)
    }

    private val sessionCallback = object: MediaSessionCompat.Callback() {
        override fun onStop() {
            super.onStop()
            // The onStop() callback should call stopSelf(). If the service was started, this stops it. In addition, the service is destroyed if there are no activities bound to it. Otherwise, the service remains bound until all its activities unbind. (If a subsequent startService() call is received before the service is destroyed, the pending stop is cancelled.)
            stopSelf()
            Log.d(MediaPlaybackService::class.java.simpleName, "Mano onStop")
        }

        override fun onPause() {
            super.onPause()
            Log.d(MediaPlaybackService::class.java.simpleName, "Mano onPause")
        }

        override fun onPlay() {
            super.onPlay()
            /*
            * The media session onPlay() callback should include code that calls startService(). This ensures that the service starts and continues to run, even when all UI MediaBrowser activities that are bound to it unbind.
            * */
            // TODO: add the
            Log.d(MediaPlaybackService::class.java.simpleName, "Mano onPlay")
        }
    }

}