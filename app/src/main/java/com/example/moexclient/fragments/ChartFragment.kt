package com.example.moexclient.fragments

import android.animation.Animator
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.airbnb.lottie.LottieAnimationView
import com.example.moexclient.App
import com.example.moexclient.R
import com.example.moexclient.viewmodels.*
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import javax.inject.Inject
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.gms.ads.*
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.math.abs
import com.google.android.ads.nativetemplates.TemplateView

import com.google.android.ads.nativetemplates.NativeTemplateStyle
import java.lang.NullPointerException
import android.text.format.DateFormat
import androidx.appcompat.app.AppCompatActivity


class ChartFragment : Fragment() {

    @Inject lateinit var viewModelFactory: ViewModelFactory
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
    private lateinit var moneyAnimation: LottieAnimationView
    private lateinit var adLoader: AdLoader
    private lateinit var adTemplate: TemplateView

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
        moneyAnimation = root.findViewById(R.id.money_confetti)
        adTemplate = root.findViewById(R.id.native_template)
        moneyAnimation.visibility = View.INVISIBLE
        adLoader = AdLoader.Builder(requireContext(), "ca-app-pub-5097316419453121/1903585632")
            .forNativeAd { ad : NativeAd ->
                if(isDetached) ad.destroy() else adTemplate.setNativeAd(ad)
                buttonsIsEnabled(true)
                viewModel.isAdShowing = true
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("ChartFragment", "ad failed to load")
                    buttonsIsEnabled(true)
                }
            })
            .build()

        nextButton.setOnClickListener {
            if(viewModel.isAdShowing) {
                viewModel.isAdShowing = false
            }
            if(viewModel.checkAdCounter()) {
                setupAdUi()
                loadAd()
            } else {
                resetUi()
                viewModel.updateChart()
            }
            viewModel.incAdCounter()
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
            viewModel.buyBtn.value = View.INVISIBLE
            viewModel.sellBtn.value = View.VISIBLE
            chart.axisLeft.removeAllLimitLines()
            chart.axisLeft.addLimitLine(limitLine(viewModel.game.stocksPrice, true))
        }
        sellButton.setOnClickListener {
            viewModel.sellAll()
            viewModel.moneyLoc.value = "BANK"
            viewModel.sellBtn.value = View.INVISIBLE
            viewModel.buyBtn.value = View.VISIBLE
            chart.axisLeft.addLimitLine(limitLine(viewModel.game.stocksPrice, false))
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
            if(it) {
                chart.axisLeft.removeAllLimitLines()
            } else {
                if(viewModel.adState.value == View.VISIBLE) {
                    setupAdUi()
                } else {
                    setupNextUi()
                    chart.invalidate()
                }
            }

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
        val isNewRecordObserver = Observer<Boolean> {
            if(it) {
                moneyAnimation.visibility = View.VISIBLE
                moneyAnimation.playAnimation()
            }
        }
        val adStateObserver = Observer<Int> {
            adTemplate.visibility = it
        }
        val chartStateObserver = Observer<Int> {
            chart.visibility = it
        }

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
        viewModel.isNewRecord.observe(viewLifecycleOwner, isNewRecordObserver)
        viewModel.adState.observe(viewLifecycleOwner, adStateObserver)
        viewModel.chartState.observe(viewLifecycleOwner, chartStateObserver)
        if(viewModel.prices.isEmpty()) {
            resetUi()
            viewModel.updateChart()
        }
        if(toggleButton.isVisible) {
            nextButton.visibility = View.INVISIBLE
        } else {
            nextButton.visibility = View.VISIBLE
        }
        if(resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            (activity as AppCompatActivity).supportActionBar?.hide()
        }
        setHasOptionsMenu(true)
        return root
    }

    override fun onResume() {
        super.onResume()
        if(viewModel.isAdShowing) {
            setupAdUi()
            loadAd()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        adTemplate.destroyNativeAd()
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
            R.id.menu_records -> {
                val direction = ChartFragmentDirections.actionChartFragmentToRecordsListFragment()
                NavHostFragment.findNavController(this).navigate(direction)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadAd() {
        adLoader.loadAd(AdRequest.Builder().build())
        buttonsIsEnabled(false)
    }

    private fun limitLine(y: Float, dashed: Boolean): LimitLine {
        val ll = LimitLine(y)
        ll.lineWidth = 2f
        ll.lineColor = Color.BLACK
        if(dashed) ll.enableDashedLine(10f, 10f, 0f)
        return ll
    }

    private fun resetUi() {
        nextButton.visibility = View.INVISIBLE
        viewModel.toggleBtn.value = ToggleState(View.VISIBLE, false)
        viewModel.moneyLoc.value = "BANK"
        viewModel.buyBtn.value = View.VISIBLE
        viewModel.sellBtn.value = View.INVISIBLE
        viewModel.adState.value = View.INVISIBLE
        viewModel.chartState.value = View.VISIBLE
    }
    private fun setupNextUi() {
        viewModel.chartState.value = View.VISIBLE
        viewModel.adState.value = View.INVISIBLE
        viewModel.buyBtn.value = View.INVISIBLE
        viewModel.sellBtn.value = View.INVISIBLE
        nextButton.visibility = View.VISIBLE
        viewModel.toggleBtn.value = ToggleState(View.INVISIBLE, false)
    }
    private fun setupAdUi() {
        setupNextUi()
        viewModel.chartState.value = View.INVISIBLE
        viewModel.adState.value = View.VISIBLE
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
            profitTv.text = String.format(getString(R.string.pos_profit), abs(profit))
        } else {
            profitTv.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_light))
            profitTv.text = String.format(getString(R.string.neg_profit), abs(profit))
        }
    }

    private fun buttonsIsEnabled(isEnabled: Boolean) {
        buyButton.isEnabled = isEnabled
        sellButton.isEnabled = isEnabled
        toggleButton.isEnabled = isEnabled
        nextButton.isEnabled = isEnabled
    }

    class DateValueFormatter: ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val date = Date(value.toLong())
            val month = DateFormat.format("MMM", date) as String
            val year = DateFormat.format("yyyy", date) as String
            return month.take(3).uppercase() + year
        }
    }


}