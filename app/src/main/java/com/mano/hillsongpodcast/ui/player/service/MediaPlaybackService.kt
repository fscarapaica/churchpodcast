package com.mano.hillsongpodcast.ui.player.service

import android.app.PendingIntent
import android.content.Context
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.media.MediaBrowserServiceCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.audio.AudioAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob


private const val LOG_TAG = "MediaPlaybackService"
private const val MY_EMPTY_MEDIA_ROOT_ID = "media_root_id"

class MediaPlaybackService : MediaBrowserServiceCompat() {

    private val serviceJob = SupervisorJob()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

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

        val sessionActivityPendingIntent =
            packageManager?.getLaunchIntentForPackage(packageName)?.let { sessionIntent ->
                PendingIntent.getActivity(this, 0, sessionIntent, 0)
            }

        // Create a new MediaSession.
        mediaSession = MediaSessionCompat(this, "MusicService")
            .apply {
                setSessionActivity(sessionActivityPendingIntent)
                isActive = true
            }

        sessionToken = mediaSession.sessionToken


        /*mediaSession = MediaSessionCompat(baseContext, LOG_TAG).apply {

            // Enable callbacks from MediaButtons and TransportControls
            setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                    or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )

            // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player
            stateBuilder = PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY
                        or PlaybackStateCompat.ACTION_PLAY_PAUSE
                )
            setPlaybackState(stateBuilder.build())

            // MySessionCallback() has methods that handle callbacks from a media controller
            setCallback(MySessionCallback())

            // Set the session's token so that client activities can communicate with it.
            setSessionToken(sessionToken)
        }*/

    }

    override fun onLoadChildren(
        parentId: String,
        result: Result<MutableList<MediaBrowserCompat.MediaItem>>
    ) {
        result.sendResult(null)
    }

    override fun onGetRoot(
        clientPackageName: String,
        clientUid: Int,
        rootHints: Bundle?
    ): BrowserRoot? {
        // Clients can connect, but this BrowserRoot is an empty hierachy
        // so onLoadChildren returns nothing. This disables the ability to browse for content.
        return BrowserRoot(MY_EMPTY_MEDIA_ROOT_ID, null)
    }

}

class MySessionCallback : MediaSessionCompat.Callback() {

    override fun onPause() {
        super.onPause()
    }

    override fun onPlay() {
        super.onPlay()
    }
}