package com.mano.churchpodcast.ui.player

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragmentX
import com.mano.churchpodcast.R
import com.mano.churchpodcast.constant.YOUTUBE_API_KEY
import com.mano.churchpodcast.databinding.FragmentYoutubePlayerBinding
import com.mano.churchpodcast.ext.navigateYoutubePlayer
import com.mano.churchpodcast.model.YoutubeVideo
import com.mano.churchpodcast.ui.adapter.YoutubeVideoAdapter

class YoutubePlayerFragment: Fragment(), YouTubePlayer.OnInitializedListener {

    companion object {
        const val YOUTUBE_VIDEO_KEY = "youtube_video_key"
        const val REQUEST_CODE = 0
    }

    private val youtubePlayerViewModel: YoutubePlayerViewModel by viewModels()
    private lateinit var fragmentBinding: FragmentYoutubePlayerBinding
    private var youtubeVideo: YoutubeVideo? = null

    private var youTubePlayer: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        youtubeVideo = arguments?.getParcelable(YOUTUBE_VIDEO_KEY)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        fragmentBinding = FragmentYoutubePlayerBinding.inflate(inflater)

        (childFragmentManager.findFragmentById(R.id.youtube_player_view) as YouTubePlayerFragmentX).apply {
            initialize(YOUTUBE_API_KEY, this@YoutubePlayerFragment)
        }

        fragmentBinding.exoTitle.text = youtubeVideo?.title
        fragmentBinding.exoDescription.text = youtubeVideo?.description

        fragmentBinding.motionBase.apply {
            addTransitionListener(transitionListener)
            setOnSwipeTouchListener {
                findNavController().navigateUp()
            }
        }

        fragmentBinding.btClose.setOnClickListener {
            findNavController().navigateUp()
        }

        fragmentBinding.btPlayPause.setOnClickListener {
            youTubePlayer?.apply {
                if (isPlaying) {
                    pause()
                } else play()
            }
        }

        val youtubeVideoAdapter = YoutubeVideoAdapter(youtubePlayerViewModel)
        fragmentBinding.rvNextVideo.run {
            layoutManager = LinearLayoutManager(context)
            adapter = youtubeVideoAdapter
        }

        youtubePlayerViewModel.youtubeVideoList.observe(viewLifecycleOwner, { youtubeVideoList ->
            youtubeVideoAdapter.submitList(youtubeVideoList)
        })

        youtubePlayerViewModel.currentYoutubeVideo.observe(viewLifecycleOwner, { currentVideo ->
            if (currentVideo != youtubeVideo) {
                navigateYoutubePlayer(currentVideo)
            }
        })

        youtubePlayerViewModel.currentYoutubeVideo.value = youtubeVideo

        return fragmentBinding.root
    }

    override fun onInitializationSuccess(
        provider: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?,
        wasRestored: Boolean
    ) {
        youTubePlayer?.setPlayerStateChangeListener(playerStateChangeListener)
        youTubePlayer?.setPlaybackEventListener(playbackEventListener)

        if (!wasRestored) {
            youTubePlayer?.cueVideo(youtubeVideo?.id)
            youTubePlayer?.setShowFullscreenButton(true)
        }
        this.youTubePlayer = youTubePlayer
    }

    override fun onInitializationFailure(
        provider: YouTubePlayer.Provider?,
        youTubeInitializationResult: YouTubeInitializationResult?
    ) {
        if (youTubeInitializationResult?.isUserRecoverableError == true) {
            youTubeInitializationResult.getErrorDialog(activity, REQUEST_CODE).show()
        } else {
            val errorMessage = "There was an error initializing the YoutubePlayer ($youTubeInitializationResult)"
            Toast.makeText(fragmentBinding.root.context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    private val playbackEventListener = object: YouTubePlayer.PlaybackEventListener {

        override fun onSeekTo(position: Int) {}

        override fun onBuffering(isBuffering: Boolean) {}

        override fun onPlaying() {
            fragmentBinding.btPlayPause.isSelected = true
        }

        override fun onStopped() {}

        override fun onPaused() {
            fragmentBinding.btPlayPause.isSelected = false
        }

    }

    private val playerStateChangeListener = object: YouTubePlayer.PlayerStateChangeListener {

        override fun onError(errorReason: YouTubePlayer.ErrorReason?) {
            Toast.makeText(
                requireContext(),
                "Playback Error: $errorReason",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onLoading() {}

        override fun onLoaded(p0: String?) {}

        override fun onAdStarted() {}

        override fun onVideoStarted() {}

        override fun onVideoEnded() {}

    }

    private val transitionListener = object: MotionLayout.TransitionListener {

        override fun onTransitionCompleted(motionLayout: MotionLayout?, constraintId: Int) {
            when(constraintId) {
                R.id.cs_youtube_player_start -> {
                    youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT)
                }
                R.id.cs_youtube_player_end -> {
                    youTubePlayer?.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS)
                }
            }
        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) { }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }

    }

}
