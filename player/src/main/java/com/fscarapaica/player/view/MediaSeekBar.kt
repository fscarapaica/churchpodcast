package com.fscarapaica.player.view

import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar

class MediaSeekBar : AppCompatSeekBar {

    private var mMediaController: MediaControllerCompat? = null
    private var mControllerCallback: ControllerCallback? = null

    private var mIsTracking = false

    private var mProgressAnimator: ValueAnimator? = null
    var timeEventListener: TimeEventListener? = null

    interface TimeEventListener {
        fun onTimeUpdate(timeMs: Int)
        fun onDurationUpdate(durationMs: Int)
    }

    private val mOnSeekBarChangeListener: OnSeekBarChangeListener =
        object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) = Unit

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                mIsTracking = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mMediaController?.transportControls?.seekTo(progress.toLong())
                updateProgress(progress)
                mIsTracking = false
            }
        }

    constructor(context: Context) : super(context) {
        super.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        super.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        super.setOnSeekBarChangeListener(mOnSeekBarChangeListener)
    }

    override fun setOnSeekBarChangeListener(l: OnSeekBarChangeListener) {
        // Prohibit adding seek listeners to this subclass.
        throw UnsupportedOperationException("Cannot add listeners to a MediaSeekBar")
    }

    fun setMediaController(mediaController: MediaControllerCompat?) {
        if (mediaController != null) {
            mControllerCallback = ControllerCallback().also {
                mediaController.registerCallback(it)
                it.onMetadataChanged(mediaController.metadata)
                it.onPlaybackStateChanged(mediaController.playbackState)
            }
        } else if (mMediaController != null) {
            mMediaController!!.unregisterCallback(mControllerCallback!!)
            mControllerCallback = null
        }
        mMediaController = mediaController
    }

    fun disconnectController() {
        if (mMediaController != null) {
            mMediaController!!.unregisterCallback(mControllerCallback!!)
            mControllerCallback = null
            mMediaController = null
        }
        clearProgressAnimator()
    }

    private fun updateProgress(currentProgress: Int) {
        if (progress != currentProgress) {
            progress = currentProgress
            timeEventListener?.onTimeUpdate(currentProgress)
        }
    }

    fun clearProgressAnimator() {
        mProgressAnimator?.apply {
            cancel()
            mProgressAnimator = null
        }
    }

    private inner class ControllerCallback : MediaControllerCompat.Callback(),
        AnimatorUpdateListener {

        override fun onPlaybackStateChanged(state: PlaybackStateCompat) {
            super.onPlaybackStateChanged(state)

            clearProgressAnimator()

            updateProgress(state.position.toInt())

            if (state.state == PlaybackStateCompat.STATE_PLAYING) {
                val timeToEnd = ((max - progress) / state.playbackSpeed).toInt()
                mProgressAnimator = ValueAnimator.ofInt(progress, max).apply {
                    duration = timeToEnd.toLong()
                    interpolator = LinearInterpolator()
                    addUpdateListener(this@ControllerCallback)
                    start()
                }
            }
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat) {
            super.onMetadataChanged(metadata)
            metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION).toInt().let { newMax ->
                max = newMax
                timeEventListener?.onDurationUpdate(newMax)
            }
            updateProgress(0)
        }

        override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
            // If the user is changing the slider, cancel the animation.
            if (mIsTracking) {
                valueAnimator.cancel()
                return
            }
            updateProgress(valueAnimator.animatedValue as Int)
        }

    }

}