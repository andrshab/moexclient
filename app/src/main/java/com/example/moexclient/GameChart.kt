package com.example.moexclient

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.renderer.CombinedChartRenderer

class GameChart: CombinedChart {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private val durationMillis = 10000
    private val gameAnimator = GameAnimator(durationMillis) {
        postInvalidate()
    }
    init {
        mRenderer = CombinedChartRenderer(this, gameAnimator, mViewPortHandler)
    }
    fun start() {
        gameAnimator.start()
    }
    fun pause() {
        gameAnimator.pause()
    }
    fun resume() {
        gameAnimator.resume()
    }
    fun isPaused(): Boolean {
        return gameAnimator.isPaused()
    }

}
class GameAnimator(durationMillis: Int, listener: AnimatorUpdateListener): ChartAnimator(listener) {
    private val animatorX: ObjectAnimator  = xAnimator(durationMillis);
    private var mListener: AnimatorUpdateListener = listener
    init {
        animatorX.addUpdateListener(mListener)
    }
    fun start() {
        animatorX.start()
    }
    fun pause() {
        animatorX.pause()
    }
    fun resume() {
        animatorX.resume()
    }
    fun isPaused(): Boolean {
        return animatorX.isPaused
    }
    private fun xAnimator(duration: Int): ObjectAnimator {
        val animatorX = ObjectAnimator.ofFloat(this, "phaseX", 0f, 1f)
        animatorX.interpolator = Easing.Linear
        animatorX.duration = duration.toLong()
        return animatorX
    }
}