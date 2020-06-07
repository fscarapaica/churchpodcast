package com.fscarapaica.player

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.session.PlaybackStateCompat
import com.fscarapaica.player.extension.fullDescription
import com.fscarapaica.player.extension.getMediaMetadataCompat
import com.google.android.exoplayer2.ControlDispatcher
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource

class PodcastPlaybackPreparer(
    private val exoPlayer: ExoPlayer,
    private val dataSourceFactory: DataSource.Factory) : MediaSessionConnector.PlaybackPreparer{

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle) = Unit

    override fun onPrepareFromMediaId(mediaId: String, playWhenReady: Boolean, extras: Bundle) = Unit

    override fun onPrepare(playWhenReady: Boolean) = Unit

    override fun getSupportedPrepareActions(): Long = PlaybackStateCompat.ACTION_PREPARE_FROM_URI or
            PlaybackStateCompat.ACTION_PREPARE_FROM_URI

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle) {
        val audioSource: MediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .setTag(extras.getMediaMetadataCompat()?.fullDescription())
            .createMediaSource(uri)

        exoPlayer.playWhenReady = true
        exoPlayer.prepare(audioSource)
    }

    override fun onCommand(
        player: Player,
        controlDispatcher: ControlDispatcher,
        command: String,
        extras: Bundle,
        cb: ResultReceiver
    ) = false

}