package com.example.moexclient

import android.animation.ObjectAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.util.AttributeSet
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.renderer.CombinedChartRenderer
import kotlin.math.abs

class GameChart: CombinedChart {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    lateinit var phaseXListener: (phaseX: Float)->Unit
    var phaseX: Float = 0f

    private val durationMillis = 60000
    val xValues: MutableList<Float> = mutableListOf()
    var entries: MutableList<Entry> = mutableListOf()
    private val gameAnimator = GameAnimator(durationMillis) {animation ->
        postInvalidate()
        phaseX = animation.getAnimatedValue("phaseX") as Float
        val min = xAxis.axisMinimum
        val max = xAxis.axisMaximum
        val goal = phaseX * (max - min) + min
        val closestEntry = entries.closestEntryWithX(goal)
        if(entries.indexOf(closestEntry)-1>0) {
            phaseXListener.invoke(entries[entries.indexOf(closestEntry)-1].y)
        }

    }
    init {
        mRenderer = CombinedChartRenderer(this, gameAnimator, mViewPortHandler)
    }
    fun prepareAnimator() {
        entries = (data.lineData.dataSets[0] as LineDataSet).values
        for(entry in entries) {
            xValues.add(entry.x)
        }
    }
    private fun List<Entry>.closestEntryWithX(value: Float) = minByOrNull { abs(value - it.x) }
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