package com.mano.churchpodcast.ui.layout

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

class MotionLayoutExo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0): MotionLayoutX(context, attrs, defStyle) {

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        return false
    }

}