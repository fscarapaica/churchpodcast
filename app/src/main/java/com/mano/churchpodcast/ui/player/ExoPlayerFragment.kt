package com.mano.churchpodcast.ui.player

import android.content.ComponentName
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.fscarapaica.player.extension.*
import com.fscarapaica.player.service.MediaPlaybackService
import com.fscarapaica.player.view.MediaSeekBar
import com.mano.churchpodcast.R
import com.mano.churchpodcast.databinding.FragmentExoPlayerBinding
import com.mano.churchpodcast.ext.shareText
import com.mano.churchpodcast.model.MediaItem

class ExoPlayerFragment: Fragment() {

    private lateinit var fragmentBinding: FragmentExoPlayerBinding
    private lateinit var mediaBrowser: MediaBrowserCompat
    private lateinit var mediaController: MediaControllerCompat

    private var mediaItem: MediaItem? = null

    companion object {
        const val MEDIA_ITEM_KEY = "media_item_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaItem = arguments?.getParcelable(MEDIA_ITEM_KEY)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentExoPlayerBinding.inflate(inflater)

        mediaBrowser = MediaBrowserCompat(context,
            ComponentName(requireContext(), MediaPlaybackService::class.java),
            connectionCallBacks,
            null
        )

        fragmentBinding.include.exoProgress.timeEventListener = object : MediaSeekBar.TimeEventListener {
            override fun onTimeUpdate(timeMs: Int) {
                fragmentBinding.include.exoPosition.text = timeMs.formatElapsedTimeMS()
            }

            override fun onDurationUpdate(durationMs: Int) {
                fragmentBinding.include.exoDuration.text = durationMs.formatElapsedTimeMS()
            }
        }

        ControlsClickListener().let { clickListener ->
            fragmentBinding.include.apply {
                exoPlayPause.setOnClickListener(clickListener)
                exoFfwd.setOnClickListener(clickListener)
                exoRew.setOnClickListener(clickListener)
            }
            fragmentBinding.includeCollapsed.apply {
                exoPlayPause.setOnClickListener(clickListener)
                exoStop.setOnClickListener {
                    closeExoPlayer()
                }
            }
        }

        fragmentBinding.ivMinimize.setOnClickListener {
            fragmentBinding.motionBase.transitionToEnd()
        }

        fragmentBinding.ivShare.setOnClickListener {
            mediaItem?.link?.let { text -> shareText(text) }
        }

        fragmentBinding.motionBase.apply {
            addTransitionListener(transitionListener)
            setOnSwipeTouchListener {
                closeExoPlayer()
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(onBackPressedCallback)

        return fragmentBinding.root
    }

    private fun closeExoPlayer() {
        mediaController.transportControls?.stop()
        findNavController().navigateUp()
    }

    override fun onStart() {
        super.onStart()
        mediaBrowser.connect()
    }

    override fun onResume() {
        super.onResume()
        activity?.apply {
            volumeControlStream = AudioManager.STREAM_MUSIC
        }
    }

    override fun onStop() {
        MediaControllerCompat.getMediaController(requireActivity())?.unregisterCallback(controllerCallback)
        fragmentBinding.include.exoProgress.disconnectController()
        fragmentBinding.exoProgressCollapsed.disconnectController()
        mediaBrowser.disconnect()
        super.onStop()
    }

    private var controllerCallback = object : MediaControllerCompat.Callback() {

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            metadata?.let { _ ->
                Glide.with(requireContext())
                    .load(metadata.displayIconUri)
                    .into(fragmentBinding.ivMediaImage)

                fragmentBinding.include.apply {
                    exoTitle.text = metadata.displayTitle
                    exoDescription.text = metadata.composedDescription
                }
                fragmentBinding.includeCollapsed.apply {
                    exoTitle.text = metadata.displayTitle
                }
            }
        }

        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            fragmentBinding.include.exoPlayPause.isSelected = state?.state == PlaybackStateCompat.STATE_PLAYING
            fragmentBinding.includeCollapsed.exoPlayPause.isSelected = state?.state == PlaybackStateCompat.STATE_PLAYING
        }

        private val MediaMetadataCompat.composedDescription
            get() = "${bundle.getString(MediaMetadataCompat.METADATA_KEY_AUTHOR)} · " +
                    "${bundle.getString(MediaMetadataCompat.METADATA_KEY_DATE)} · " +
                    "$displayDescription "

    }

    private val connectionCallBacks = object : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaBrowser.sessionToken.also { token ->
                mediaController = MediaControllerCompat(requireContext(), token)
                mediaController.registerCallback(controllerCallback)
                MediaControllerCompat.setMediaController(requireActivity(), mediaController)
                fragmentBinding.include.exoProgress.setMediaController(mediaController)
                controllerCallback.onMetadataChanged(mediaController.metadata)
                controllerCallback.onPlaybackStateChanged(mediaController.playbackState)
                if (mediaController.metadata.containsKey(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)
                    && mediaController.metadata.id == mediaItem?.id.toString()) {
                    return
                }
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
                MediaControllerCompat.getMediaController(activity!!)
                    .transportControls
                    .prepareFromUri(Uri.parse(mediaItem?.mediaLink), bundle)
            }
        }

        override fun onConnectionSuspended() {
            // TODO: Disable the user UI since the service has crashed
        }

        override fun onConnectionFailed() {
            // TODO: the service has refused our connection
        }
    }

    private inner class ControlsClickListener : View.OnClickListener {
        override fun onClick(view: View) {
            MediaControllerCompat
                .getMediaController(activity!!)?.let { mediaController ->
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

    private val transitionListener = object: MotionLayout.TransitionListener {

        override fun onTransitionCompleted(motionLayout: MotionLayout?, constraintId: Int) {
            when(constraintId) {
                R.id.cs_youtube_player_start -> {
                    onBackPressedCallback.isEnabled = true
                    fragmentBinding.include.exoProgress.setMediaController(mediaController)
                    fragmentBinding.exoProgressCollapsed.disconnectController()
                }
                R.id.cs_youtube_player_end -> {
                    onBackPressedCallback.isEnabled = false
                    fragmentBinding.include.exoProgress.disconnectController()
                    fragmentBinding.exoProgressCollapsed.setMediaController(mediaController)
                }
            }
        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }

    }

    val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            fragmentBinding.motionBase.apply {
                if (currentState == startState) {
                    transitionToEnd()
                } else activity?.onBackPressed()
            }
        }
    }

    fun Int.formatElapsedTimeMS(): String = DateUtils.formatElapsedTime(this/MS_BASE)

}

private const val MS_BASE = 1000L
