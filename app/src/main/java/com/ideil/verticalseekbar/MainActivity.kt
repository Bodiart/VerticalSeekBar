package com.ideil.verticalseekbar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
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
        seekbar.setPoints(
            arrayListOf(
                VerticalSeekBarPoint("Point1", "1", 100.0),
                VerticalSeekBarPoint("Point2", "2", 200.0),
                VerticalSeekBarPoint("Point3", "3", 300.0),
                VerticalSeekBarPoint("Point4", "4", 400.0),
                VerticalSeekBarPoint("Point5", "5", 500.0)
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