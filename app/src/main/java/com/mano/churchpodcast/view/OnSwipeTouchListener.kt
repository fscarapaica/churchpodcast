package com.mano.churchpodcast.view

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.*
import android.view.View
import android.view.ViewConfiguration
import kotlin.math.abs

internal open class OnSwipeTouchListener(c: Context,val view: View) : View.OnTouchListener {

    private val gestureDetector: GestureDetector
    private val swipeThreshold: Int = ViewConfiguration.get(c).scaledPagingTouchSlop * 3

    private var currentDownEvent: MotionEvent? = null

    var isGestureEnable = false

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return if (isGestureEnable) {
            if (motionEvent.action == ACTION_DOWN) {
                currentDownEvent = obtain(motionEvent)
            } else if (motionEvent.action == ACTION_UP && currentDownEvent != null) {
                val diffX = motionEvent.rawX - currentDownEvent!!.rawX
                if (abs(diffX) > swipeThreshold) {
                    if (diffX > 0) {
                        onSwipeRight()
                    }
                } else view.translationX = 0f
            }
            gestureDetector.onTouchEvent(motionEvent)
        } else {
            if (view.translationX != 0f) {
                view.translationX = 0f
            }
            false
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val diffX = e2.x - e1.x
            val relativePosition = view.translationX + diffX
            if (relativePosition > 0) {
                view.translationX = relativePosition
            } else view.translationX = 0f
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }

    open fun onSwipeRight() { }

    init {
        gestureDetector = GestureDetector(c, GestureListener())
    }
}