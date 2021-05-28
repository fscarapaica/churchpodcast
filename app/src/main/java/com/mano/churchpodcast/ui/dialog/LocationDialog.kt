package com.mano.churchpodcast.ui.dialog

import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.animation.doOnEnd
import com.mano.churchpodcast.ext.startCircularHideAnimation
import com.mano.churchpodcast.ext.startCircularRevealAnimation

class LocationPopup(view: View) : PopupWindow(view) {

    private var isRunningExitAnimation = false

    init {
        width = LinearLayout.LayoutParams.MATCH_PARENT
        height = LinearLayout.LayoutParams.WRAP_CONTENT
        isFocusable = true
    }

    fun show(anchorView: View) {
        contentView.post {
            contentView.startCircularRevealAnimation()
        }
        showAsDropDown(anchorView)
    }

    override fun dismiss() {
        if (contentView.isAttachedToWindow) {
            if (!isRunningExitAnimation) {
                contentView.startCircularHideAnimation().doOnEnd {
                    super.dismiss()
                }
                isRunningExitAnimation = true
            }
        } else {
            super.dismiss()
        }
    }
}