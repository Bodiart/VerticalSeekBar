package com.ideil.vertical_seekbar.extansions

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.AnimRes
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop


fun View.VSBSetGone(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View.VSBSetVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
}

fun Boolean?.VSBGetVisibility(): Int {
    return if (this == true) View.VISIBLE else View.INVISIBLE
}

fun View.VSBSetMarginStart(marginStart: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(marginStart, marginTop, marginRight, marginBottom)
    this.layoutParams = menuLayoutParams
}

fun View.VSBSetMarginTop(marginTop: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBottom)
    this.layoutParams = menuLayoutParams
}

fun View.VSBSetMarginEnd(marginEnd: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(marginLeft, marginTop, marginEnd, marginBottom)
    this.layoutParams = menuLayoutParams
}

fun View.VSBSetMarginBot(marginBot: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(marginLeft, marginTop, marginRight, marginBot)
    this.layoutParams = menuLayoutParams
}

fun View.VSBSetMargins(marginStart: Int, marginTop: Int, marginEnd: Int, marginBot: Int) {
    val menuLayoutParams = this.layoutParams as ViewGroup.MarginLayoutParams
    menuLayoutParams.setMargins(marginStart, marginTop, marginEnd, marginBot)
    this.layoutParams = menuLayoutParams
}

fun View.VSBSetPaddingStart(paddingStart: Int) {
    this.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
}

fun View.VSBSetPaddingTop(paddingTop: Int) {
    this.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
}

fun View.VSBSetPaddingEnd(paddingEnd: Int) {
    this.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
}

fun View.VSBSetPaddingBot(paddingBottom: Int) {
    this.setPadding(paddingStart, paddingTop, paddingEnd, paddingBottom)
}

fun View.VSBStartAnim(@AnimRes animRes: Int): Animation {
    val anim = AnimationUtils.loadAnimation(this.context, animRes)
    this.startAnimation(anim)
    return anim
}

fun View.VSBSetHeight(newHeight: Int) {
    val params = this.layoutParams as ViewGroup.LayoutParams
    params.height = newHeight
    this.layoutParams = params
}

fun View.VSBSetWidth(newWidth: Int) {
    val params = this.layoutParams as ViewGroup.LayoutParams
    params.width = newWidth
    this.layoutParams = params
}

fun Animation.VSBSetListener(
    startCallback: (() -> Unit)? = null,
    repeatCallback: (() -> Unit)? = null,
    endCallback: (() -> Unit)? = null
) {
    this.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {
            repeatCallback?.invoke()
        }

        override fun onAnimationEnd(animation: Animation?) {
            endCallback?.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            startCallback?.invoke()
        }
    })
}