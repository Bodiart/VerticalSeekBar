package com.ideil.vertical_seekbar.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.TextView
import com.ideil.vertical_seekbar.R
import com.ideil.vertical_seekbar.config.VSBConfig
import com.ideil.vertical_seekbar.extansions.VBSLog

@Suppress("PrivatePropertyName")
@SuppressLint("ViewConstructor")
class VerticalSeekBarDivision constructor(
    context: Context,
    val pointName: String,
    val progressStep: Float,
    val progress: Int,
    var config: VSBConfig
): FrameLayout(context) {
    private val tag = VerticalSeekBarDivision::class.java.simpleName


    init {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.el_vertical_seek_bar_division, this)
        setupDistance()
        setupTranslationY()
        setupConfig(config)
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
     * Public func-s
     * */

    fun setupConfig(config: VSBConfig) {
        this.config = config
        fl_seek_bar_division?.background = config.divisionBackground
        tv_vertical_seek_bar_division_text?.setTextColor(config.colorDivisionText)
        tv_vertical_seek_bar_division_text?.setTextSize(TypedValue.COMPLEX_UNIT_PX, config.dimenDivisionTextSizePx)
    }

    /**
     * View getters
     * */

    private val tv_vertical_seek_bar_division_text: TextView?
        get() = findViewById(R.id.tv_vertical_seek_bar_division_text)

    private val fl_seek_bar_division: FrameLayout?
        get() = findViewById(R.id.fl_seek_bar_division)

}