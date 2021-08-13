package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
        greenButton.setOnClickListener{
            viewModel.isTrueColor(Color.GREEN)
            viewModel.updateChart()
        }
        redButton.setOnClickListener{
            viewModel.isTrueColor(Color.RED)
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

}