package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.moexclient.data.MoexRepository
import com.example.moexclient.viewmodels.ChartViewModel
import com.example.moexclient.viewmodels.ChartViewModelFactory
import com.example.moexclient.viewmodels.NewsViewModel
import com.example.moexclient.viewmodels.NewsViewModelFactory
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel
    lateinit var blueButton: Button
    lateinit var chart: LineChart
    lateinit var secNameTv: TextView

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
        blueButton = root.findViewById(R.id.blue_button)
        blueButton.setOnClickListener{updateChart()}
        val chartDataObserver = Observer<LineData> {
            chart.data = it
            chart.invalidate()
        }
        val secNameObserver = Observer<String> { secNameTv.text = it }
        viewModel.chartData.observe(viewLifecycleOwner, chartDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        return root
    }

    private fun updateChart() {
        viewModel.updateChart()
    }

}