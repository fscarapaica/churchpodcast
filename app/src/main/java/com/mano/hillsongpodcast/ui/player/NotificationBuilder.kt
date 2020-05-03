package com.mano.hillsongpodcast.ui.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle
import androidx.media.session.MediaButtonReceiver
import com.bumptech.glide.Glide
import com.mano.hillsongpodcast.R
import com.mano.hillsongpodcast.ui.player.extensions.isPlayEnabled
import com.mano.hillsongpodcast.ui.player.extensions.isPlaying

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

const val NOW_PLAYING_CHANNEL: String = "com.mano.hillsongpodcast.player.NOW_PLAYING"
const val NOW_PLAYING_NOTIFICATION: Int = 0xb339

/**
 * Helper class to encapsulate code for building notifications.
 */
class NotificationBuilder(private val context: Context) {
    private val platformNotificationManager: NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val rewindAction = NotificationCompat.Action(
        R.drawable.exo_controls_rewind,
        //context.getString(R.string.notification_skip_to_previous),
        "REWIND",
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_REWIND))
    private val playAction = NotificationCompat.Action(
        R.drawable.exo_controls_play,
        //context.getString(R.string.notification_play),
        "PLAY",
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_PLAY))
    private val pauseAction = NotificationCompat.Action(
        R.drawable.exo_controls_pause,
        //context.getString(R.string.notification_pause),
        "PAUSE",
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_PAUSE))
    private val fastForwardAction = NotificationCompat.Action(
        R.drawable.exo_controls_fastforward,
        //context.getString(R.string.notification_skip_to_next),
        "FAST_FORWARD",
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_FAST_FORWARD))
    private val stopPendingIntent =
        MediaButtonReceiver.buildMediaButtonPendingIntent(context, ACTION_STOP)

    suspend fun buildNotification(sessionToken: MediaSessionCompat.Token): Notification {
        if (shouldCreateNowPlayingChannel()) {
            createNowPlayingChannel()
        }

        val controller = MediaControllerCompat(context, sessionToken)
        val description = controller.metadata.description
        val playbackState = controller.playbackState

        val builder = NotificationCompat.Builder(context, NOW_PLAYING_CHANNEL)

        // Only add actions for skip back, play/pause, skip forward, based on what's enabled.
        builder.addAction(rewindAction)
        if (playbackState.isPlaying) {
            builder.addAction(pauseAction)
        } else if (playbackState.isPlayEnabled) {
            builder.addAction(playAction)
        }
        builder.addAction(fastForwardAction)

        val mediaStyle = MediaStyle()
            .setCancelButtonIntent(stopPendingIntent)
            .setMediaSession(sessionToken)
            .setShowActionsInCompactView(1)
            .setShowCancelButton(true)

        val largeIconBitmap = description.iconUri?.let {
            resolveUriAsBitmap(context, it)
        }

        return builder.setContentIntent(controller.sessionActivity)
            .setContentText(description.subtitle)
            .setContentTitle(description.title)
            .setDeleteIntent(stopPendingIntent)
            .setLargeIcon(largeIconBitmap)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setStyle(mediaStyle)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun shouldCreateNowPlayingChannel() =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && !nowPlayingChannelExists()

    @RequiresApi(Build.VERSION_CODES.O)
    private fun nowPlayingChannelExists() =
        platformNotificationManager.getNotificationChannel(NOW_PLAYING_CHANNEL) != null

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNowPlayingChannel() {
        val notificationChannel = NotificationChannel(
            NOW_PLAYING_CHANNEL,
            //context.getString(R.string.notification_channel),
            "Now Playing",
            NotificationManager.IMPORTANCE_LOW)
            .apply {
                //description = context.getString(R.string.notification_channel_description)
                description = "Shows what is currently playing in Hillsong Podcast"
            }

        platformNotificationManager.createNotificationChannel(notificationChannel)
    }

    private suspend fun resolveUriAsBitmap(context: Context, url: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            Glide.
                with(context).
                asBitmap().
                load(url).
                submit()
                .get()
        }
    }

}