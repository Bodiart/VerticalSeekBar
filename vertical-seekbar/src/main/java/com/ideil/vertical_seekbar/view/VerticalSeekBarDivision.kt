package com.ideil.vertical_seekbar.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import android.widget.TextView
import com.ideil.vertical_seekbar.R
import com.ideil.vertical_seekbar.extansions.VBSLog

@Suppress("PrivatePropertyName")
@SuppressLint("ViewConstructor")
class VerticalSeekBarDivision constructor(
    context: Context,
    val pointName: String,
    val progressStep: Float,
    val progress: Int
): FrameLayout(context) {
    private val tag = VerticalSeekBarDivision::class.java.simpleName


    init {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.el_vertical_seek_bar_division, this)
        setupDistance()
        setupTranslationY()
    }

    /**
     * Help fun-s
     * */

    private fun setupDistance() {
        tv_vertical_seek_bar_division_text?.text = pointName
    }

    private fun setupTranslationY() {
        val marginBot = progress * progressStep * -1
        VBSLog(tag, "newTranslation=$marginBot")
        translationY = marginBot
    }

    /**
     * View getters
     * */

    private val tv_vertical_seek_bar_division_text: TextView?
        get() = findViewById(R.id.tv_vertical_seek_bar_division_text)

}