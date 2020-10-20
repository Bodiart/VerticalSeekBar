package com.ideil.vertical_seekbar.extansions

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat


fun VBSLog(tag: String, text: String, ex: Throwable? = null) {
    if (ex == null)
        Log.d(tag, text)
    else
        Log.d(tag, text, ex)
}

fun Int.getColor(context: Context): Int? {
    return ContextCompat.getColor(context, this)
}

fun Int.getDrawable(context: Context): Drawable? {
    return ContextCompat.getDrawable(context, this)
}

fun Int.getDimen(context: Context): Float {
    return context.resources.getDimension(this)
}