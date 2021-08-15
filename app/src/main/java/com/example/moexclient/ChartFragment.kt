package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moexclient.viewmodels.ChartViewModel
import com.example.moexclient.viewmodels.ChartViewModelFactory
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.CombinedData
import javax.inject.Inject
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener

import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel
    private lateinit var nextButton: Button
    private lateinit var chart: GameChart
    private lateinit var secNameTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity?.applicationContext as App).appComponent.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[ChartViewModel::class.java]
        val root = inflater.inflate(R.layout.fragment_chart, container, false)
        chart = root.findViewById(R.id.chart)
        chart.axisRight.isEnabled = false
        chart.axisLeft.setDrawAxisLine(false)
        chart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        chart.xAxis.setDrawGridLines(false)
        chart.xAxis.valueFormatter = DateValueFormatter()
        chart.xAxis.labelCount = 3
        chart.legend.isEnabled = false
        chart.description.text = ""
        chart.onChartGestureListener = object : OnChartTapListener{
            override fun onChartSingleTapped(me: MotionEvent?) {
                val touchPoint = chart.getValuesByTouchPoint(me?.x?:0f, me?.y?:0f, YAxis.AxisDependency.LEFT)
                Log.d("ChartFragment", "x = ${touchPoint.x.toFloat()}, y = ${touchPoint.y.toFloat()}")
                val ll = LimitLine(touchPoint.y.toFloat())
                ll.lineWidth = 1f
                ll.lineColor = Color.BLACK
                chart.axisLeft.addLimitLine(ll)
            }
        }
        secNameTv = root.findViewById(R.id.sec_name_tv)
        nextButton = root.findViewById(R.id.next_button)

        setupNextUi()

        nextButton.setOnClickListener {
            if(chart.isPaused()) {
                chart.resume()
            } else {
                chart.pause()
            }

        }

        val chartDataObserver = Observer<CombinedData> {
            chart.data = it
            chart.start()
        }
        val secNameObserver = Observer<String> { secNameTv.text = it }
        viewModel.chartData.observe(viewLifecycleOwner, chartDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        if(chart.isEmpty) {
            viewModel.updateChart()
        }
        return root
    }

    private fun resetUi() {
        nextButton.visibility = View.GONE
    }
    private fun setupNextUi() {
        nextButton.visibility = View.VISIBLE
    }

}