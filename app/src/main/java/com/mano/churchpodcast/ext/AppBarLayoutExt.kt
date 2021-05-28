package com.mano.churchpodcast.ext

import androidx.appcompat.widget.Toolbar
import com.google.android.material.appbar.AppBarLayout

fun Toolbar.isScrollEnable(enable: Boolean) {
    (layoutParams as AppBarLayout.LayoutParams).scrollFlags =
        if (enable)
            AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        else
            AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
}