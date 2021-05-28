package com.mano.churchpodcast.ui.layout

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import com.mano.churchpodcast.R
import com.mano.churchpodcast.view.OnSwipeTouchListener

open class MotionLayoutX @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
): MotionLayout(context, attrs, defStyle) {

    private var isTouchInside = false
    private var isForward: Boolean = true
    private var layoutEventListener: (() -> Unit?)? = null
    private var containerView: View? = null

    private var onSwipeTouchListener =
        object : OnSwipeTouchListener(context, rootView) {
            override fun onSwipeRight() {
                layoutEventListener?.invoke()
            }
        }

    var isSwipeEventEnable: Boolean = false
        set(value) {
            onSwipeTouchListener.isGestureEnable = value
            field = value
        }

    private val customTransitionListener = object: TransitionListener {

        override fun onTransitionCompleted(motionLayout: MotionLayout?, constraintId: Int) {
            when(constraintId) {
                R.id.cs_youtube_player_transition -> {
                    motionLayout?.apply {
                        isForward = if (isForward) {
                            setTransition(R.id.t_youtube_player_down_resize)
                            transitionToEnd()
                            false
                        } else {
                            setTransition(R.id.t_youtube_player_down_move)
                            progress = 1f
                            transitionToStart()
                            true
                        }
                    }
                }
                R.id.cs_youtube_player_start -> {
                    isSwipeEventEnable = false
                }
                R.id.cs_youtube_player_end -> {
                    isSwipeEventEnable = true
                }
            }
        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) { }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            when(p2) {
                R.id.cs_youtube_player_end -> {
                    isSwipeEventEnable = false
                }
            }
        }

        override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) { }

    }

    fun setOnSwipeTouchListener(layoutEventListener: () -> Unit) {
        this.layoutEventListener = layoutEventListener
        setOnTouchListener(onSwipeTouchListener)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        containerView = findViewById(R.id.player_container)
        setTransitionListener(customTransitionListener)
    }

    private fun View?.isTouchEventInside(ev: MotionEvent): Boolean {
        this?.run {
            if (ev.x > left && ev.x < right) {
                if (ev.y > top && ev.y < bottom) {
                    return true
                }
            }
        }
        return false
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            isTouchInside = containerView.isTouchEventInside(event)
        }

        return if (isTouchInside) super.onTouchEvent(event) else false
    }

}