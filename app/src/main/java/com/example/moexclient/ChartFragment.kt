package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel
    lateinit var greenButton: Button
    lateinit var redButton: Button
    lateinit var nextButton: Button
    lateinit var chart: CombinedChart
    lateinit var secNameTv: TextView
    lateinit var statisticsTv: TextView

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
        secNameTv = root.findViewById(R.id.sec_name_tv)
        greenButton = root.findViewById(R.id.green_button)
        redButton = root.findViewById((R.id.red_button))
        statisticsTv = root.findViewById(R.id.statistics_tv)
        nextButton = root.findViewById(R.id.next_button)

        setupSelectUi()

        greenButton.setOnClickListener{
            setStatsColor(viewModel.isTrueColor(Color.GREEN))
            setupNextUi()
            viewModel.showAnswer()
            chart.invalidate()
        }
        redButton.setOnClickListener{
            setStatsColor(viewModel.isTrueColor(Color.RED))
            setupNextUi()
            viewModel.showAnswer()
            chart.invalidate()
        }
        nextButton.setOnClickListener {
            setupSelectUi()
            viewModel.updateChart()
        }

        val chartDataObserver = Observer<CombinedData> {
            chart.data = it
            chart.invalidate()
        }
        val secNameObserver = Observer<String> { secNameTv.text = it }
        val xAxisMaxObserver = Observer<Float> { chart.xAxis.axisMaximum = it }
        val statisticsObserver = Observer<Int> { statisticsTv.text = "$it%" }
        viewModel.chartData.observe(viewLifecycleOwner, chartDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        viewModel.xAxisMax.observe(viewLifecycleOwner, xAxisMaxObserver)
        viewModel.statistics.observe(viewLifecycleOwner, statisticsObserver)
        if(chart.isEmpty) {
            viewModel.updateChart()
        }
        return root
    }

    private fun setupSelectUi() {
        greenButton.visibility = View.VISIBLE
        redButton.visibility = View.VISIBLE
        nextButton.visibility = View.GONE
    }
    private fun setupNextUi() {
        greenButton.visibility = View.GONE
        redButton.visibility = View.GONE
        nextButton.visibility = View.VISIBLE
    }
    private fun setStatsColor(isAnswerTrue: Boolean) {
        if(isAnswerTrue){
            statisticsTv.setTextColor(Color.GREEN)
        } else {
            statisticsTv.setTextColor(Color.RED)
        }
    }

}