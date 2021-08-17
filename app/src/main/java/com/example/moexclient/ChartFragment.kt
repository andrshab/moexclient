package com.example.moexclient

import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.moexclient.viewmodels.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import javax.inject.Inject
import com.github.mikephil.charting.data.LineData
import kotlin.math.abs
import kotlin.math.floor


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
    private lateinit var profitTv: TextView
    private lateinit var progressBar: ProgressBar

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
        profitTv = root.findViewById(R.id.profit_tv)
        progressBar = root.findViewById(R.id.progress_bar)

        nextButton.setOnClickListener {
            resetUi()
            viewModel.updateChart()
        }
        toggleButton.setOnCheckedChangeListener { button, isChecked ->
            viewModel.toggleBtn.value = ToggleState(button.visibility, isChecked)
            if(isChecked){
                viewModel.showNextPrice()
            }
        }
        buyButton.setOnClickListener {
            viewModel.buyAll()
            viewModel.moneyLoc.value = "STOCKS"
            viewModel.buyBtn.value = View.GONE
            viewModel.sellBtn.value = View.VISIBLE
            chart.axisLeft.removeAllLimitLines()
            chart.axisLeft.addLimitLine(limitLine(viewModel.game.stocksPrice, true))
        }
        sellButton.setOnClickListener {
            viewModel.sellAll()
            viewModel.moneyLoc.value = "BANK"
            viewModel.sellBtn.value = View.GONE
            viewModel.buyBtn.value = View.VISIBLE
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
        val buyBtnObserver = Observer<Int> { buyButton.visibility = it }
        val sellBtnObserver = Observer<Int> { sellButton.visibility = it }
        val toggleButtonObserver = Observer<ToggleState> {
            toggleButton.visibility = it.vis
            toggleButton.isChecked = it.isChecked
        }
        val isLoadingObserver = Observer<Boolean> {
            buttonsIsEnabled(!it)
            if(it) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.INVISIBLE
            }
        }
        val profitObserver = Observer<Float> { setProfitTv(it) }

        viewModel.priceData.observe(viewLifecycleOwner, priceDataObserver)
        viewModel.secName.observe(viewLifecycleOwner, secNameObserver)
        viewModel.chartEdge.observe(viewLifecycleOwner, chartEdgesObserver)
        viewModel.isGameRunning.observe(viewLifecycleOwner, isGameRunningObserver)
        viewModel.sum.observe(viewLifecycleOwner, sumObserver)
        viewModel.startSum.observe(viewLifecycleOwner, startSumObserver)
        viewModel.buyBtn.observe(viewLifecycleOwner, buyBtnObserver)
        viewModel.sellBtn.observe(viewLifecycleOwner, sellBtnObserver)
        viewModel.toggleBtn.observe(viewLifecycleOwner, toggleButtonObserver)
        viewModel.isLoading.observe(viewLifecycleOwner, isLoadingObserver)
        viewModel.profit.observe(viewLifecycleOwner, profitObserver)
        if(viewModel.prices.isEmpty()) {
            resetUi()
            viewModel.updateChart()
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_news -> {
                val direction = ChartFragmentDirections.actionChartFragmentToNewsListFragment()
                NavHostFragment.findNavController(this).navigate(direction)
            }
            R.id.menu_settings -> {
                val direction = ChartFragmentDirections.actionChartFragmentToSettingsFragment()
                NavHostFragment.findNavController(this).navigate(direction)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun limitLine(y: Float, dashed: Boolean): LimitLine {
        val ll = LimitLine(y)
        ll.lineWidth = 2f
        ll.lineColor = Color.BLACK
        if(dashed) ll.enableDashedLine(10f, 10f, 0f)
        return ll
    }

    private fun resetUi() {
        nextButton.visibility = View.GONE
        viewModel.toggleBtn.value = ToggleState(View.VISIBLE, false)
        viewModel.moneyLoc.value = "BANK"
        viewModel.buyBtn.value = View.VISIBLE
        viewModel.sellBtn.value = View.GONE
    }
    private fun setupNextUi() {
        viewModel.buyBtn.value = View.GONE
        viewModel.sellBtn.value = View.GONE
        nextButton.visibility = View.VISIBLE
        viewModel.toggleBtn.value = ToggleState(View.GONE, false)
    }

    private fun setSumTv(sum: Float) {
        sumTv.text = sum.toString()
        if(sum >= viewModel.game.startSum) {
            sumTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
        } else {
            sumTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
        }
    }

    private fun setProfitTv(profit: Float) {
        if(profit >= 0) {
            profitTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_light))
            profitTv.text = "+${abs(profit)}%"
        } else {
            profitTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
            profitTv.text = "-${abs(profit)}%"
        }
    }

    private fun buttonsIsEnabled(isEnabled: Boolean) {
        buyButton.isEnabled = isEnabled
        sellButton.isEnabled = isEnabled
        toggleButton.isEnabled = isEnabled
        nextButton.isEnabled = isEnabled
    }


}