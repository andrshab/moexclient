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
import com.github.mikephil.charting.components.XAxis
import javax.inject.Inject
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.math.floor


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ChartViewModelFactory
    private lateinit var viewModel: ChartViewModel
    private lateinit var nextButton: Button
    private lateinit var chart: LineChart
    private lateinit var secNameTv: TextView
    private lateinit var stocksTv: TextView
    private lateinit var toggleButton: ToggleButton
    private lateinit var buyButton: Button
    private lateinit var sellButton: Button
    private lateinit var bankTv: TextView
    private lateinit var stocksNp: NumberPicker
    private lateinit var hintTv: TextView
    private lateinit var sumTv: TextView
    private lateinit var startSumTv: TextView

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
        buyButton = root.findViewById(R.id.buy_button)
        sellButton = root.findViewById(R.id.sell_button)
        stocksTv = root.findViewById(R.id.stocks_tv)
        toggleButton = root.findViewById(R.id.toggle_button)
        bankTv = root.findViewById(R.id.bank_tv)
        stocksNp = root.findViewById(R.id.number_picker)
        hintTv = root.findViewById(R.id.stocks_hint)
        sumTv = root.findViewById(R.id.sum_tv)
        startSumTv = root.findViewById(R.id.startsum_tv)
        stocksNp.maxValue = 100
        stocksNp.minValue = 0
        stocksNp.value = 10
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
            viewModel.buy(stocksNp.value)
        }
        sellButton.setOnClickListener {
            viewModel.sell(stocksNp.value)
        }
        stocksNp.setOnValueChangedListener { picker, oldVal, newVal ->
            hintTv.text = "On sum: ${viewModel.game.stocksPrice * newVal}"
        }
        val priceDataObserver = Observer<LineData> {
            val curPrice = viewModel.currentPrice(
                it.getDataSetByLabel("primary", true) as LineDataSet
            )
            val curStocksNumber = viewModel.game.stocksNumber
            val sum = curPrice * curStocksNumber + viewModel.game.bank

            hintTv.text = "On sum: ${viewModel.game.stocksPrice * stocksNp.value}"
            bankTv.text = "${viewModel.game.bank}: ${floor(viewModel.game.bank/sum*100)}%"
            stocksTv.text = "${curPrice * curStocksNumber}: ${floor(curPrice * curStocksNumber/sum*100)}%"
            startSumTv.text = viewModel.game.startSum.toString()
            sumTv.text = sum.toString()
            if(sum >= viewModel.game.startSum) {
                sumTv.setBackgroundColor(Color.GREEN)
            } else {
                sumTv.setBackgroundColor(Color.RED)
            }
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
        val bankObserver = Observer<Float> { bankTv.text = "${it}: ${floor(it/(viewModel.game.stocks + it)*100)}%"}
        val stocksObserver = Observer<Float> { stocksTv.text = "${it}: ${floor(it/(it + viewModel.game.bank)*100)}%" }
        val sumObserver = Observer<Float> {
            startSumTv.text = viewModel.game.startSum.toString()
            sumTv.text = it.toString()
            if(it >= viewModel.game.startSum) {
                sumTv.setBackgroundColor(Color.GREEN)
            } else {
                sumTv.setBackgroundColor(Color.RED)
            }
        }

        viewModel.priceData.observe(viewLifecycleOwner, priceDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        viewModel.chartEdge.observe(viewLifecycleOwner, chartEdgesObserver)
        viewModel.isFinished.observe(viewLifecycleOwner, isFinishedObserver)
        viewModel.bank.observe(viewLifecycleOwner, bankObserver)
        viewModel.stocks.observe(viewLifecycleOwner, stocksObserver)
        viewModel.sum.observe(viewLifecycleOwner, sumObserver)
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