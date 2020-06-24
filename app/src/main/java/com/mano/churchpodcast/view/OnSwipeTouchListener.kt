package com.mano.churchpodcast.view

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_UP
import android.view.View
import kotlin.math.abs

// TODO: REWORK THE SWIPE ANIMATION
internal open class OnSwipeTouchListener(c: Context,val view: View) : View.OnTouchListener {
    private val gestureDetector: GestureDetector
    private val resetX = view.x

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        if (motionEvent.action == ACTION_UP) {
            view.translationX = resetX
        }
        return gestureDetector.onTouchEvent(motionEvent)
    }



    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD: Int = 100
        private val SWIPE_VELOCITY_THRESHOLD: Int = 100

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            try {
                val diffX = e2.x - e1.x
                if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        onSwipeRight()
                    }
                    else {
                        onSwipeLeft()
                    }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (e1 != null && e2 != null) {
                val diffX = e2.x - e1.x
                view.translationX = view.translationX + diffX
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }
    open fun onSwipeRight() {}
    open fun onSwipeLeft() {}

    init {
        gestureDetector = GestureDetector(c, GestureListener())
    }
}