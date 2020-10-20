package com.ideil.vertical_seekbar.extansions

import android.util.Log


fun VBSLog(tag: String, text: String, ex: Throwable? = null) {
    if (ex == null)
        Log.d(tag, text)
    else
        Log.d(tag, text, ex)
}