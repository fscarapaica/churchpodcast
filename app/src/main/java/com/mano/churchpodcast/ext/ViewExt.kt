package com.mano.churchpodcast.ext

import android.animation.Animator
import android.view.View
import android.view.ViewAnimationUtils
import kotlin.math.hypot

fun View.startCircularRevealAnimation() = ViewAnimationUtils.createCircularReveal(
        this,
        0,
        0,
        0f,
        hypot(width.toDouble(), height.toDouble()).toFloat()
    ).apply {
        duration = 400
    }.start()

fun View.startCircularHideAnimation(): Animator = ViewAnimationUtils.createCircularReveal(
        this,
        0,
        0,
        hypot(width.toDouble(), height.toDouble()).toFloat(),
        0f
    ).apply {
        duration = 400
        start()
    }