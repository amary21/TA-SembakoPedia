package com.rivaldomathindas.sembakopedia.fragment

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.model.DetailProduct
import com.rivaldomathindas.sembakopedia.utils.K.STATISTIC_PRODUCT
import com.rivaldomathindas.sembakopedia.utils.TimeFormatter
import kotlinx.android.synthetic.main.fragment_stats_monthly.*

class StatsMonthlyFragment : Fragment() {
    private var paramStats: DetailProduct? = null

    companion object {

        @JvmStatic
        fun newInstance(paramStats: DetailProduct) =
            StatsMonthlyFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(STATISTIC_PRODUCT, paramStats)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            paramStats = it.getSerializable(STATISTIC_PRODUCT) as DetailProduct
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats_monthly, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // mpChart Data
        val yValues = mutableListOf<Entry>()
        val labels = mutableListOf<String>()
        labels.add("")
        var indexLabel = 0

        val dataStatistics = paramStats?.product?.groupBy { product ->
            product.time?.let {TimeFormatter().isThisMont(it) } }
        dataStatistics?.forEach { key, value ->
            // list data price
            val priceData = arrayListOf<Float>()
            value.forEach { product ->
                product.price?.toFloat()?.let { priceData.add(it) }
            }

            // average data price if size > 1
            val avgPrice = if (value.size > 1) {
                priceData.sum() / value.size
            } else {
                priceData[0]
            }

            // add label with date and indexLabel
            key?.let { labels.add(it) }
            indexLabel += 1

            // add y value
            yValues.add(Entry(indexLabel.toFloat(), avgPrice))
        }

        // add and custom line dataset
        val lineDataSets = LineDataSet(yValues, "")
        lineDataSets.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSets.axisDependency = YAxis.AxisDependency.LEFT
        lineDataSets.color = requireContext().getColor(R.color.chartColor)
        lineDataSets.fillAlpha = 255
        lineDataSets.setDrawFilled(true)
        lineDataSets.fillColor = requireContext().getColor(R.color.chartColor)

        // add dataset to chart
        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(lineDataSets)
        val data = LineData(dataSets)
        mpMonthLineChartStatistics.data = data

        // custom chart
        mpMonthLineChartStatistics.setBackgroundColor(Color.WHITE)
        mpMonthLineChartStatistics.setGridBackgroundColor(Color.WHITE)
        mpMonthLineChartStatistics.description.isEnabled = false
        mpMonthLineChartStatistics.axisRight.setDrawLabels(false)
        mpMonthLineChartStatistics.setDrawGridBackground(true)
        mpMonthLineChartStatistics.setDrawBorders(true)
        mpMonthLineChartStatistics.setPinchZoom(false)
        mpMonthLineChartStatistics.setTouchEnabled(false)

        // remove legend
        val legend = mpMonthLineChartStatistics.legend
        legend.isEnabled = false

        // add cubic chart
        val xAxis = mpMonthLineChartStatistics.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = labels.size
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
    }
}