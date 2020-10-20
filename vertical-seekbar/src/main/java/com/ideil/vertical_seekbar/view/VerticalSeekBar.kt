package com.ideil.vertical_seekbar.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.ideil.vertical_seekbar.R
import com.ideil.vertical_seekbar.config.VSBConfig
import com.ideil.vertical_seekbar.entity.VerticalSeekBarPoint
import com.ideil.vertical_seekbar.extansions.VSBSetListener
import com.ideil.vertical_seekbar.extansions.VSBSetMarginBot
import com.ideil.vertical_seekbar.extansions.VSBStartAnim
import com.ideil.vertical_seekbar.extansions.VBSLog
import kotlin.math.absoluteValue
import kotlin.math.roundToInt


/**
 * A nicer, redesigned and vertical SeekBar
 */
@Suppress("PrivatePropertyName")
open class VerticalSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private val tag = VerticalSeekBar::class.java.simpleName

    // callbacks
    var onProgressChangeListener: ((Int, VerticalSeekBarPoint?) -> Unit)? = null
    var onPressListener: ((Int, VerticalSeekBarPoint?) -> Unit)? = null
    var onReleaseListener: ((Int, VerticalSeekBarPoint?) -> Unit)? = null

    // points
    private var pointList = arrayListOf(
        VerticalSeekBarPoint("testPoint1", "P1", 100.0),
        VerticalSeekBarPoint("testPoint2", "P2", 200.0),
        VerticalSeekBarPoint("testPoint3", "P3", 300.0),
        VerticalSeekBarPoint("testPoint4", "P4", 400.0),
        VerticalSeekBarPoint("testPoint5", "P5", 500.0)
    )
    private var pointListNames = pointList.map { it.name }
    private var barCount = pointList.size

    // progress
    private val maxProgress = barCount - 1
    private var progress = 0

    // btn
    private var btnTouchStartYDelta = 0f
    private var btnTouched = false
    private var btnObjectAnimator: ObjectAnimator? = null

    // description
    private var descriptionObjectAnimator: ValueAnimator? = null
    private val descriptionElevation = 8f

    // bg
    private var seekBarBgHeight = 0

    // divisions
    private val divisionsList = ArrayList<VerticalSeekBarDivision>(barCount)


    init {
        init(context)
    }

    private fun init(context: Context) {
        inflate(context, R.layout.el_vertical_seek_bar, this)
        setupTouchListeners()
        setupClickListeners()
        setupSeekBarBgHeight()
        addDivisions()
        setupStartPoint()
    }

    private fun setupTouchListeners() {
        setupBtnTouchListener()
        setupBgTouchListener()
    }

    /**
     * Btn touch listener START
     * */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupBtnTouchListener() {
        cv_vertical_seek_bar_btn_card?.setOnTouchListener { _, event ->
            val rawY = event.rawY
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> btnTouchActionDown(rawY)
                MotionEvent.ACTION_MOVE -> btnTouchActionMove(rawY)
                MotionEvent.ACTION_UP -> btnTouchActionUp()
            }
            true
        }
    }

    private fun btnTouchActionDown(rawY: Float) {
        btnTouched = true
        btnTouchStartYDelta = rawY - (cv_vertical_seek_bar_btn_card?.translationY ?: 0f)
        showRadiusDescription()
        onPressListener?.invoke(progress, pointList.getOrNull(progress))
    }

    private fun btnTouchActionMove(rawY: Float) {
        btnTouchActionMoveTranslateBtn(rawY)
        btnTouchActionMoveCalculateProgress()
    }

    private fun btnTouchActionMoveTranslateBtn(rawY: Float, moveDescription: Boolean = true) {
        val btnTranslation = btnTouchStartYDelta - rawY
        if (isValidBtnTranslationY(btnTranslation)) {
            cv_vertical_seek_bar_btn_card?.translationY = -btnTranslation
            if (moveDescription)
                cv_vertical_seek_bar_description_card?.VSBSetMarginBot(btnTranslation.toInt() * 2)
        }
    }

    private fun btnTouchActionMoveCalculateProgress() {
        val fabPosition = cv_vertical_seek_bar_btn_card?.translationY?.absoluteValue ?: 0f
        val step = seekBarBgHeight / maxProgress
        if (step != 0)
            updateProgress(validateProgress((fabPosition / step).roundToInt()))
    }

    private fun btnTouchActionUp() {
        btnTouched = false
        updateBtnTranslationY()
        btnTouchStartYDelta = 0f
        hideRadiusDescription()
        onReleaseListener?.invoke(progress, pointList.getOrNull(progress))
    }
    /**
     * Btn touch listener END
     * */

    /**
     * BG touch listener START
     * */
    @SuppressLint("ClickableViewAccessibility")
    private fun setupBgTouchListener() {
        fl_vertical_seek_bar_bg_layout?.setOnTouchListener { _, event ->
            val positionY = event.y.roundToInt()
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> bgTouchActionDown(positionY)
                MotionEvent.ACTION_UP -> bgTouchActionUp(positionY)
            }
            true
        }
    }

    private fun bgTouchCalculateProgress(positionY: Int) {
        val fillHeight = seekBarBgHeight
        when { // here we update progress
            positionY in 1 until fillHeight -> {
                val newValue = maxProgress - (positionY.toFloat() * maxProgress / fillHeight)
                updateProgress(newValue.roundToInt())
            }
            positionY <= 0 -> updateProgress(maxProgress)
            positionY >= fillHeight -> updateProgress(0)
        }
    }

    private fun bgTouchActionDown(positionY: Int) {
        bgTouchCalculateProgress(positionY)
        onPressListener?.invoke(progress, pointList.getOrNull(progress))
    }

    private fun bgTouchActionUp(positionY: Int) {
        bgTouchCalculateProgress(positionY)
        onReleaseListener?.invoke(progress, pointList.getOrNull(progress))
        updateBtnTranslationY()
    }

    /**
     * BG touch listener END
     * */

    private fun setupClickListeners() {
        view_vertical_seek_bar_top?.setOnClickListener {
            updateProgress(maxProgress)
            updateBtnTranslationY()
        }
        view_vertical_seek_bar_bot?.setOnClickListener {
            updateProgress(0)
            updateBtnTranslationY()
        }
    }

    /**
     * Help functions
     * */
    private fun setupSeekBarBgHeight() {
        post {
            seekBarBgHeight = fl_vertical_seek_bar_bg_layout?.measuredHeight ?: 0
        }
    }

    private fun addDivisions() {
        post {
            val step = (seekBarBgHeight.toFloat() / maxProgress)
            (0..maxProgress).forEach { progress ->
                val division = VerticalSeekBarDivision(
                    context,
                    pointList[progress].name,
                    step,
                    progress
                )
                divisionsList.add(division)
                fl_vertical_seek_bar_division_container?.addView(division)
            }
        }
    }

    private fun setupStartPoint() {
        tv_vertical_seek_bar_chosen_radius?.text = pointList.getOrNull(0)?.name
    }

    private fun updateProgress(newProgress: Int) {
        if (newProgress == progress) return
        VBSLog(tag, "newProgress=$newProgress")
        progress = newProgress
        updateChosenRadiusText()
        updateChosenRadiusDescriptionText()
        onProgressChangeListener?.invoke(progress, pointList.getOrNull(progress))
    }

    private fun updateChosenRadiusText(changeDescriptionText: Boolean = true) {
        tv_vertical_seek_bar_chosen_radius ?: return

        val currNameInd = progress
        val prevNameInd: Int = pointListNames.indexOf(tv_vertical_seek_bar_chosen_radius?.text)
        if (prevNameInd == -1) return
        if (currNameInd == prevNameInd) return

        var startAnimRes = R.anim.vertical_seek_bar_slide_out_bot
        var endAnimRes = R.anim.vertical_seek_bar_slide_in_top

        if (currNameInd > prevNameInd) {
            startAnimRes = R.anim.vertical_seek_bar_slide_out_top
            endAnimRes = R.anim.vertical_seek_bar_slide_in_bot
        }

        val delay = if (btnTouched) 0L else 50L

        Handler(Looper.getMainLooper()).postDelayed({
            tv_vertical_seek_bar_chosen_radius?.VSBStartAnim(startAnimRes)
                ?.VSBSetListener(endCallback = {
                    tv_vertical_seek_bar_chosen_radius?.text =
                        pointListNames.getOrNull(currNameInd)
                    tv_vertical_seek_bar_chosen_radius?.VSBStartAnim(endAnimRes)
                })

            if (changeDescriptionText) {
                tv_vertical_seek_bar_chosen_radius_description?.VSBStartAnim(startAnimRes)
                    ?.VSBSetListener(endCallback = {
                        tv_vertical_seek_bar_chosen_radius_description?.text =
                            pointListNames.getOrNull(currNameInd)
                        tv_vertical_seek_bar_chosen_radius_description?.VSBStartAnim(endAnimRes)
                    })
            }
        }, delay)
    }

    private fun updateChosenRadiusDescriptionText() {
//        tv_vertical_seek_bar_chosen_radius_description?.text = distanceList.getOrNull(progress)
    }

    private fun updateBtnTranslationY() {
        val fillHeight = seekBarBgHeight
        val progressHeight = fillHeight * (progress.toFloat() / maxProgress.toFloat())
        setFabTranslationY(-progressHeight)
    }

    private fun setFabTranslationY(translationY: Float, setDescTranslationY: Boolean = true) {
        btnObjectAnimator?.cancel()
        btnObjectAnimator = ObjectAnimator.ofFloat(
            cv_vertical_seek_bar_btn_card,
            "translationY",
            translationY
        ).apply {
            duration = if (alpha > 0f) // don't animate if view invisible
                200
            else
                0
            addUpdateListener {
                val value = it.animatedValue as Float
                if (setDescTranslationY)
                    cv_vertical_seek_bar_description_card?.VSBSetMarginBot(value.toInt() * -2)
            }
        }
        btnObjectAnimator?.start()
    }

    private fun validateProgress(newProgress: Int): Int {
        return when {
            newProgress < 0 -> 0
            newProgress > maxProgress -> maxProgress
            else -> newProgress
        }
    }

    private fun isValidBtnTranslationY(translationY: Float): Boolean {
        return translationY <= seekBarBgHeight && translationY >= 0
    }

    private fun showRadiusDescription() {
        descriptionObjectAnimator?.cancel()
        descriptionObjectAnimator = ValueAnimator.ofFloat(
            cv_vertical_seek_bar_description_card?.scaleX ?: 1f,
            1f
        ).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                cv_vertical_seek_bar_description_card?.scaleX = value
                cv_vertical_seek_bar_description_card?.scaleY = value
                cv_vertical_seek_bar_description_card?.cardElevation = descriptionElevation * value
            }
            duration = 150
            startDelay = if (cv_vertical_seek_bar_description_card?.scaleX != 0f
                || cv_vertical_seek_bar_description_card?.scaleY != 0f
            )
                0L
            else
                200L
        }
        descriptionObjectAnimator?.start()
    }

    private fun hideRadiusDescription() {
        descriptionObjectAnimator?.cancel()
        descriptionObjectAnimator = ValueAnimator.ofFloat(
            cv_vertical_seek_bar_description_card?.scaleX ?: 1f,
            0f
        ).apply {
            addUpdateListener {
                val value = it.animatedValue as Float
                cv_vertical_seek_bar_description_card?.scaleX = value
                cv_vertical_seek_bar_description_card?.scaleY = value
                cv_vertical_seek_bar_description_card?.cardElevation = descriptionElevation * value
            }
            duration = 150
            startDelay = if (cv_vertical_seek_bar_description_card?.scaleX ?: 1f < 1f
                || cv_vertical_seek_bar_description_card?.scaleY ?: 1f < 1f
            )
                0L
            else
                200L
        }
        descriptionObjectAnimator?.start()
    }

    /**
     * Public functions
     * */

    fun changeProgress(progress: Int) {
        post {
            updateProgress(progress)
            updateBtnTranslationY()
        }
    }

    fun setPoints(points: ArrayList<VerticalSeekBarPoint>) {
        pointList = points
        pointListNames = pointList.map { it.name }
        barCount = pointList.size
        setupStartPoint()
    }

    /**
     * Config setup
     * */

    fun setupConfig(config: VSBConfig) {
        // drawable START
        view_vertical_seek_bar_bg?.background = config.background
        fl_seek_bar_division?.background = config.divisionBackground // todo send to division view
        // drawable END

        // colors START
        // description card
        cv_vertical_seek_bar_description_card?.setCardBackgroundColor(config.colorDescriptionCardBg)
        tv_vertical_seek_bar_chosen_radius_description?.setTextColor(config.colorDescriptionText)
        // btn
        cv_vertical_seek_bar_btn_card?.setCardBackgroundColor(config.colorBtnBg)
        tv_vertical_seek_bar_chosen_point?.setTextColor(config.colorBtnText)
        // colors END
    }

    /**
     * View getters
     * */

    private val cv_vertical_seek_bar_btn_card: CardView?
        get() = findViewById(R.id.cv_vertical_seek_bar_btn_card)

    private val cv_vertical_seek_bar_description_card: CardView?
        get() = findViewById(R.id.cv_vertical_seek_bar_description_card)

    private val fl_vertical_seek_bar_bg_layout: FrameLayout?
        get() = findViewById(R.id.fl_vertical_seek_bar_bg_layout)

    private val view_vertical_seek_bar_top: View?
        get() = findViewById(R.id.view_vertical_seek_bar_top)

    private val view_vertical_seek_bar_bot: View?
        get() = findViewById(R.id.view_vertical_seek_bar_bot)

    private val fl_vertical_seek_bar_division_container: FrameLayout?
        get() = findViewById(R.id.fl_vertical_seek_bar_division_container)

    private val tv_vertical_seek_bar_chosen_radius: TextView?
        get() = findViewById(R.id.tv_vertical_seek_bar_chosen_point)

    private val tv_vertical_seek_bar_chosen_radius_description: TextView?
        get() = findViewById(R.id.tv_vertical_seek_bar_chosen_radius_description)

    private val view_vertical_seek_bar_bg: View?
        get() = findViewById(R.id.view_vertical_seek_bar_bg)

    private val tv_vertical_seek_bar_chosen_point: TextView?
        get() = findViewById(R.id.tv_vertical_seek_bar_chosen_point)

    private val fl_seek_bar_division: FrameLayout?
        get() = findViewById(R.id.fl_seek_bar_division)

}