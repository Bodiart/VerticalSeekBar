package com.ideil.vertical_seekbar.config

import android.content.Context
import com.ideil.vertical_seekbar.R
import com.ideil.vertical_seekbar.extansions.getColor
import com.ideil.vertical_seekbar.extansions.getDrawable

class VSBConfig(context: Context) {

    /**
     * Drawable
     * */

    // background
    var background = R.drawable.vertical_seek_bar_bg.getDrawable(context)!!
    var divisionBackground = R.drawable.vertical_seek_bar_division_bg.getDrawable(context)!!

    /**
     * Colors
     * */

    // description card
    var colorDescriptionCardBg = R.color.vsb_description_card_bg.getColor(context)!!
    var colorDescriptionText = R.color.vsb_description_text.getColor(context)!!
    // btn
    var colorBtnBg = R.color.vsb_btn_bg.getColor(context)!!
    var colorBtnText = R.color.vsb_btn_text.getColor(context)!!

    /**
     * Dimens
     * */

    // divisions
//    var dimenDivisionWidth = R.dimen.vsb_division_width.getDimen(context)
//    var dimenDivisionHeight = R.dimen.vsb_division_height.getDimen(context)
//    var dimenDivisionMarginTop = R.dimen.vsb_division_height.getDimen(context)
}