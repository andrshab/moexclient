package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.NumberPicker
import android.widget.TextView
import android.widget.ToggleButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moexclient.viewmodels.ChartViewModel
import com.example.moexclient.viewmodels.ChartViewModelFactory
import com.example.moexclient.viewmodels.Edges
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import javax.inject.Inject
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.floor
import kotlin.math.round


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel
    private lateinit var nextButton: Button
    private lateinit var chart: LineChart
    private lateinit var secNameTv: TextView
    private lateinit var toggleButton: ToggleButton
    private lateinit var buyButton: Button
    private lateinit var sellButton: Button
    private lateinit var sumTv: TextView
    private lateinit var startSumTv: TextView
    private lateinit var moneyLocationTv: TextView

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
        chart.setTouchEnabled(false)
        chart.setScaleEnabled(false)
        secNameTv = root.findViewById(R.id.sec_name_tv)
        nextButton = root.findViewById(R.id.next_button)
        buyButton = root.findViewById(R.id.buy_button)
        sellButton = root.findViewById(R.id.sell_button)
        toggleButton = root.findViewById(R.id.toggle_button)
        sumTv = root.findViewById(R.id.sum_tv)
        startSumTv = root.findViewById(R.id.startsum_tv)
        moneyLocationTv = root.findViewById(R.id.money_location_tv)
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
        buyButton.setOnClickListener {
            viewModel.buyAll()
            moneyLocationTv.text = "STOCKS"
            buyButton.visibility = View.GONE
            sellButton.visibility = View.VISIBLE
        }
        sellButton.setOnClickListener {
            viewModel.sellAll()
            moneyLocationTv.text = "BANK"
            sellButton.visibility = View.GONE
            buyButton.visibility = View.VISIBLE
        }
        val priceDataObserver = Observer<LineData> {
            chart.data = it
            chart.invalidate()
            viewModel.animate(toggleButton.isChecked)
        }
        val secNameObserver = Observer<String> { secNameTv.text = it }
        val chartEdgesObserver = Observer<Edges> {
            chart.xAxis.axisMinimum = it.xMin
            chart.xAxis.axisMaximum = it.xMax
        }
        val isGameRunningObserver = Observer<Boolean> {
            val curPrice = viewModel.game.stocksPrice
            if(it) {
                chart.axisLeft.removeAllLimitLines()
                chart.axisLeft.addLimitLine(limitLine(curPrice, true))
            } else {
                chart.axisLeft.addLimitLine(limitLine(curPrice, false))
                setupNextUi()
            }
            chart.invalidate()
        }
        val sumObserver = Observer<Float> { setSumTv(it) }
        val startSumObserver = Observer<Float> { startSumTv.text = it.toString() }

        viewModel.priceData.observe(viewLifecycleOwner, priceDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        viewModel.chartEdge.observe(viewLifecycleOwner, chartEdgesObserver)
        viewModel.isGameRunning.observe(viewLifecycleOwner, isGameRunningObserver)
        viewModel.sum.observe(viewLifecycleOwner, sumObserver)
        viewModel.startSum.observe(viewLifecycleOwner, startSumObserver)
        if(chart.isEmpty) {
            viewModel.updateChart()
        }
        return root
    }

    fun limitLine(y: Float, dashed: Boolean): LimitLine {
        val ll = LimitLine(y)
        ll.lineWidth = 2f
        ll.lineColor = Color.BLACK
        if(dashed) ll.enableDashedLine(10f, 10f, 0f)
        return ll
    }

    private fun resetUi() {
        nextButton.visibility = View.GONE
        toggleButton.visibility = View.VISIBLE
        toggleButton.isChecked = false
        moneyLocationTv.text = "BANK"
        buyButton.visibility = View.VISIBLE
        sellButton.visibility = View.GONE
    }
    private fun setupNextUi() {
        nextButton.visibility = View.VISIBLE
        toggleButton.visibility = View.GONE
    }

    private fun setSumTv(sum: Float) {
        sumTv.text = sum.toString()
        if(sum >= viewModel.game.startSum) {
            sumTv.setBackgroundColor(Color.GREEN)
        } else {
            sumTv.setBackgroundColor(Color.RED)
        }
    }

}