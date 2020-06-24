package com.mano.churchpodcast.util

import android.os.Bundle

private const val DEFAULT_NAME = "object"

fun Bundle.putExtraJson(src: Any?) {
    putString(DEFAULT_NAME, GsonUtil.toJson(src))
}

fun Bundle.putExtraJson(name: String, src: Any?) {
    putString(name, GsonUtil.toJson(src))
}

fun <T> Bundle.getJsonExtra(`class`: Class<T>): T? =
    getJsonExtra(DEFAULT_NAME, `class`)

fun <T> Bundle.getJsonExtra(name: String, `class`: Class<T>): T? = getString(name)?.let {
        GsonUtil.fromJson(it, `class`)
}
