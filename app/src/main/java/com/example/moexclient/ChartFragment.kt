package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moexclient.viewmodels.ChartViewModel
import com.example.moexclient.viewmodels.ChartViewModelFactory
import com.example.moexclient.viewmodels.Edges
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import javax.inject.Inject
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel
    private lateinit var nextButton: Button
    private lateinit var chart: LineChart
    private lateinit var secNameTv: TextView
    private lateinit var stocksTv: TextView
    private lateinit var toggleButton: ToggleButton

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
        nextButton = root.findViewById(R.id.next_button)
        stocksTv = root.findViewById(R.id.stocks_tv)
        toggleButton = root.findViewById(R.id.toggle_button)
        resetUi()

        nextButton.setOnClickListener {
            resetUi()
            viewModel.updateChart()
        }
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                viewModel.showNextPrice()
            }
        }
        val priceDataObserver = Observer<LineData> {
            stocksTv.text = viewModel.currentPrice(it.getDataSetByLabel("primary", true) as LineDataSet).toString()
            chart.data = it
            chart.invalidate()
            viewModel.animate(toggleButton.isChecked)
        }
        val secNameObserver = Observer<String> { secNameTv.text = it }
        val chartEdgesObserver = Observer<Edges> {
            chart.xAxis.axisMinimum = it.xMin
            chart.xAxis.axisMaximum = it.xMax
        }
        val isFinishedObserver = Observer<Boolean> {
            if(it) setupNextUi()
        }
        viewModel.priceData.observe(viewLifecycleOwner, priceDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        viewModel.chartEdge.observe(viewLifecycleOwner, chartEdgesObserver)
        viewModel.isFinished.observe(viewLifecycleOwner, isFinishedObserver)
        if(chart.isEmpty) {
            viewModel.updateChart()
        }
        return root
    }

    private fun resetUi() {
        nextButton.visibility = View.GONE
        toggleButton.visibility = View.VISIBLE
        toggleButton.isChecked = false
    }
    private fun setupNextUi() {
        nextButton.visibility = View.VISIBLE
        toggleButton.visibility = View.GONE
    }

}