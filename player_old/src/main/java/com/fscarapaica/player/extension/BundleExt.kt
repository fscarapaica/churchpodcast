package com.fscarapaica.player.extension

import android.os.Bundle
import android.support.v4.media.MediaMetadataCompat

private const val MEDIA_METADATA = "media_metadata"

fun Bundle.putMediaMetadataCompat(mediaDescriptionCompat: MediaMetadataCompat) {
    putParcelable(MEDIA_METADATA, mediaDescriptionCompat)
}

fun Bundle.getMediaMetadataCompat() : MediaMetadataCompat? = getParcelable(MEDIA_METADATA)
