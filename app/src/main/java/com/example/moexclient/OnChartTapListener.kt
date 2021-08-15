package com.example.moexclient

import android.view.MotionEvent
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener

interface OnChartTapListener: OnChartGestureListener {
    override fun onChartDoubleTapped(me: MotionEvent?){}
    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ){}
    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ){}
    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ){}
    override fun onChartLongPressed(me: MotionEvent?){}
    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float){}
    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float){}
}