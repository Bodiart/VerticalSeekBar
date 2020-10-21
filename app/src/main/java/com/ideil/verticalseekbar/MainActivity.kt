package com.ideil.verticalseekbar

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.ideil.vertical_seekbar.config.VSBConfig
import com.ideil.vertical_seekbar.entity.VerticalSeekBarPoint
import com.ideil.vertical_seekbar.view.VerticalSeekBar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initSeekbar()
    }

    private fun initSeekbar() {
        val seekbar = findViewById<VerticalSeekBar>(R.id.vsb)
        seekbar.setupConfig(VSBConfig(this).apply {
            background = ContextCompat.getDrawable(this@MainActivity, R.drawable.vertical_seek_bar_bg_test)!!
            divisionBackground = ContextCompat.getDrawable(this@MainActivity, R.drawable.vertical_seek_bar_division_bg_test)!!

            colorDescriptionCardBg = Color.parseColor("#000000")
            colorDescriptionText = Color.parseColor("#FFFFFF")

            colorBtnBg = Color.parseColor("#000000")

            colorDivisionText = Color.parseColor("#000000")

        })
        seekbar.setPoints(
            arrayListOf(
                VerticalSeekBarPoint("Point1", "1", 100.0),
                VerticalSeekBarPoint("Point2", "2", 200.0),
                VerticalSeekBarPoint("Point3", "3", 300.0),
                VerticalSeekBarPoint("Point4", "4", 400.0),
                VerticalSeekBarPoint("Point5", "5", 500.0),
                VerticalSeekBarPoint("Point6", "6", 600.0),
                VerticalSeekBarPoint("Point7", "7", 700.0),
                VerticalSeekBarPoint("Point8", "8", 800.0),
                VerticalSeekBarPoint("Point9", "9", 900.0),
                VerticalSeekBarPoint("Point10", "10", 1000.0),
            )
        )
        seekbar.onProgressChangeListener = { progress, point ->
            findViewById<TextView>(R.id.progress_changed).text = "Progress: $progress"
            findViewById<TextView>(R.id.value).text = "Point value: ${point?.value}"
        }
        seekbar.onReleaseListener = { progress, point ->
            findViewById<TextView>(R.id.released).text = "Released: $progress"
        }
        seekbar.onPressListener = { progress, point ->
            findViewById<TextView>(R.id.press).text = "Press: $progress"
        }
    }
}