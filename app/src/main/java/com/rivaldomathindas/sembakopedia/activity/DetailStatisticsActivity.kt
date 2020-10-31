package com.rivaldomathindas.sembakopedia.activity

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.fragment.StatsAllFragment
import com.rivaldomathindas.sembakopedia.fragment.StatsMonthlyFragment
import com.rivaldomathindas.sembakopedia.fragment.StatsWeeklyFragment
import com.rivaldomathindas.sembakopedia.model.DetailProduct
import com.rivaldomathindas.sembakopedia.utils.AppUtils
import com.rivaldomathindas.sembakopedia.utils.K
import com.rivaldomathindas.sembakopedia.utils.PagerAdapter
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