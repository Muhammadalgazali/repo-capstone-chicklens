package com.example.chickenlens.view.main

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.chickenlens.R
import com.example.chickenlens.adapter.ChickenWeek
import com.example.chickenlens.adapter.SectionPaperAdapter
import com.example.chickenlens.api.weekAvgResponse
import com.example.chickenlens.databinding.ActivityMainBinding
import com.example.chickenlens.databinding.ActivityOnBoardingBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.DefaultValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.LargeValueFormatter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupViewModel()


    }

    private fun setupView() {
        // Sembunyikan status bar di atas layar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupViewModel() {
        val sectionsPaperAdapter = SectionPaperAdapter(this)
        val viewPager: ViewPager2 = binding.viewPaper
        viewPager.adapter = sectionsPaperAdapter
        val tabs: TabLayout = binding.tabs

        val tabsName = resources.getStringArray(R.array.tabs_name)

        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = tabsName[position]
        }.attach()

        val barChart: BarChart = binding.barChart

        mainViewModel.grafik.observe(this) { dataList ->
            dataList?.let {
                createChart(barChart, it)
            }
        }

        mainViewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
    }

    private fun createChart(barChart: BarChart, dataList: List<weekAvgResponse>) {
        val entriesAyam = ArrayList<BarEntry>()
        val entriesPakan = ArrayList<BarEntry>()
        val labels = ArrayList<String>()

        dataList.forEachIndexed { index, item ->
            val minggu = item.minggu?.toFloat() ?: 0f
            val ayam = item.rerataAyam?.toFloat() ?: 0f
            val pakan = item.rerataPakan?.toFloat() ?: 0f
            entriesAyam.add(BarEntry(index.toFloat(), ayam))
            entriesPakan.add(BarEntry(index.toFloat(), pakan))
            labels.add("Week ${item.minggu}")
        }

        val dataSetAyam = BarDataSet(entriesAyam, "Rata-rata Ayam")
        dataSetAyam.color = Color.parseColor("#EE9F8E")

        val dataSetPakan = BarDataSet(entriesPakan, "Rata-rata Pakan")
        dataSetPakan.color = Color.parseColor("#B5C6F6")

        val barData = BarData(dataSetAyam, dataSetPakan)

        barChart.apply {
            data = barData
            setFitBars(true)
            description.isEnabled = false
            legend.isEnabled = true
            animateY(1000)
            invalidate()

            xAxis.apply {
                valueFormatter = IndexAxisValueFormatter(labels)
                position = XAxis.XAxisPosition.TOP
                granularity = 1f
                isGranularityEnabled = true
            }

            axisLeft.apply {
                axisMinimum = 0f
                axisMaximum = getMaxValue(entriesAyam, entriesPakan) + 10f
                valueFormatter = DefaultValueFormatter(0)
            }

            axisRight.isEnabled = false
        }
    }

    private fun getMaxValue(entriesAyam: List<BarEntry>, entriesPakan: List<BarEntry>): Float {
        var max = 0f
        entriesAyam.forEach { entry ->
            if (entry.y > max) {
                max = entry.y
            }
        }
        entriesPakan.forEach { entry ->
            if (entry.y > max) {
                max = entry.y
            }
        }
        return max
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}