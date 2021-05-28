package com.mano.churchpodcast.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.fragment.app.FragmentContainerView
import com.mano.churchpodcast.R

class MotionLayoutYoutube @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0): MotionLayoutX(context, attrs, defStyle) {

    private var playerView: FragmentContainerView? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        playerView = findViewById(R.id.youtube_player_view)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        playerView?.dispatchTouchEvent(ev)
        return super.dispatchTouchEvent(ev)
    }

}