package com.mano.churchpodcast.ext

import android.content.Intent
import android.os.Bundle
import android.support.v4.media.session.MediaControllerCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.mano.churchpodcast.R
import com.mano.churchpodcast.constant.defaultNavigationOptions
import com.mano.churchpodcast.model.MediaItem
import com.mano.churchpodcast.model.YoutubeVideo
import com.mano.churchpodcast.ui.player.ExoPlayerFragment
import com.mano.churchpodcast.ui.player.YoutubePlayerFragment

fun Fragment.navigateYoutubePlayer(selectedYoutubeVideo: YoutubeVideo) {
    val bundle = Bundle().apply {
        putParcelable(YoutubePlayerFragment.YOUTUBE_VIDEO_KEY, selectedYoutubeVideo)
    }

    activity?.apply {
        MediaControllerCompat.getMediaController(requireActivity())?.transportControls?.stop()
        findNavController(R.id.nav_player_fragment).apply {
            navigateUp()
            navigate(R.id.navigation_youtube_player, bundle, defaultNavigationOptions)
        }
    }
}

fun Fragment.navigateExoPlayer(selectedMediaItem: MediaItem) {
    val bundle = Bundle().apply {
        putParcelable(ExoPlayerFragment.MEDIA_ITEM_KEY, selectedMediaItem)
    }

    activity?.findNavController(R.id.nav_player_fragment)?.apply {
        navigateUp()
        navigate(R.id.navigation_exo_player, bundle, defaultNavigationOptions)
    }
}

fun Fragment.shareText(text: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        type="text/plain"
        putExtra(Intent.EXTRA_TEXT, text)
    }
    startActivity(Intent.createChooser(shareIntent, "compartir con:"))
}