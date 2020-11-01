package com.rivaldomathindas.sembakopedia.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.android.material.tabs.TabLayout
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.fragment.StatsAllFragment
import com.rivaldomathindas.sembakopedia.fragment.StatsMonthlyFragment
import com.rivaldomathindas.sembakopedia.fragment.StatsWeeklyFragment
import com.rivaldomathindas.sembakopedia.model.DetailProduct
import com.rivaldomathindas.sembakopedia.utils.AppUtils
import com.rivaldomathindas.sembakopedia.utils.K
import com.rivaldomathindas.sembakopedia.utils.PagerAdapter
import com.rivaldomathindas.sembakopedia.utils.TimeFormatter
import kotlinx.android.synthetic.main.activity_detail_statistics.*

class DetailStatisticsActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {
    private lateinit var detailProduct: DetailProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_statistics)

        detailProduct = intent.getSerializableExtra(K.TYPE_PRODUCT) as DetailProduct

        initViews()
    }

    private fun initViews() {
        toolbarDetailStatistics.setTitleTextColor(Color.BLACK)
        setSupportActionBar(toolbarDetailStatistics)
        supportActionBar?.title = detailProduct.type.name

        tvStatistics.text = getString(R.string.statistic, detailProduct.type.name)
        tvPrediction.text = getString(R.string.prediction, detailProduct.type.name)

        setupViewPager()
        setupPredictions()
    }

    private fun setupPredictions() {
        // mpChart Data
        val yValues = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        var indexLabel = 0

        val dataFilter = detailProduct.product.groupBy { product ->
            product.time?.let {  TimeFormatter().getNormalSecondYear(it) }
        }

        val lastValue = dataFilter.values.first()
        val lastKey = dataFilter.keys.first()

        // list data price
        val priceData = arrayListOf<Float>()
        lastValue.forEach { product ->
            product.price?.toFloat()?.let { priceData.add(it) }
        }

        // average data price if size > 1
        var avgPrice = if (lastValue.size > 1) {
            priceData.sum() / lastValue.size
        } else {
            priceData[0]
        }


        while (avgPrice >= 500f){
            lastKey?.let { TimeFormatter().getNextDay(it, indexLabel) }?.let { labels.add(it) }
            yValues.add(Entry(indexLabel.toFloat(), avgPrice))

            avgPrice -= 500f
            indexLabel += 1
        }

        // add and custom line dataset
        val lineDataSets = LineDataSet(yValues, "")
        lineDataSets.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSets.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSets.color = getColor(R.color.chartColor)
        lineDataSets.fillAlpha = 255
        lineDataSets.setDrawFilled(true)
        lineDataSets.fillColor = getColor(R.color.chartColor)

        // add dataset to chart
        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(lineDataSets)
        val data = LineData(dataSets)
        mpPredictionStatistics.data = data

        // custom chart
        mpPredictionStatistics.setBackgroundColor(Color.WHITE)
        mpPredictionStatistics.setGridBackgroundColor(Color.WHITE)
        mpPredictionStatistics.description.isEnabled = false
        mpPredictionStatistics.axisRight.setDrawLabels(false)
        mpPredictionStatistics.setDrawGridBackground(true)
        mpPredictionStatistics.setDrawBorders(true)
        mpPredictionStatistics.setPinchZoom(false)
        mpPredictionStatistics.setTouchEnabled(false)

        // remove legend
        val legend = mpPredictionStatistics.legend
        legend.isEnabled = false

        // add cubic chart
        val xAxis = mpPredictionStatistics.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = labels.size
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        val weeklyFragment = StatsWeeklyFragment.newInstance(detailProduct)
        val monthlyFragment = StatsMonthlyFragment.newInstance(detailProduct)
        val allFragment = StatsAllFragment.newInstance(detailProduct)

        adapter.addFragment(weeklyFragment)
        adapter.addFragment(monthlyFragment)
        adapter.addFragment(allFragment)

        vp_statistics_detail.adapter = adapter
        vp_statistics_detail.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_statistics))

        tab_statistics.addOnTabSelectedListener(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null)
            vp_statistics_detail.currentItem = tab.position
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {}

    override fun onTabReselected(tab: TabLayout.Tab?) {}
}