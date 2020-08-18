package com.rivaldomathindas.sembakopedia.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.rivaldomathindas.sembakopedia.fragment.MyUploadsPartsFragment
import com.google.android.material.tabs.TabLayout
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.network.BaseActivity
import com.rivaldomathindas.sembakopedia.utils.AppUtils
import com.rivaldomathindas.sembakopedia.utils.PagerAdapter
import kotlinx.android.synthetic.main.activity_my_uploads.*

class MyUploadsActivity : BaseActivity(), TabLayout.OnTabSelectedListener {

    companion object {
        private const val PARTS = "PARTS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_uploads)

        initViews()
    }

    private fun initViews() {
        setSupportActionBar(toolbar2)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "My Uploads"

        setupViewPager()
        setupTabs()
        initFab()

    }

    private fun setupViewPager() {
        val adapter = PagerAdapter(supportFragmentManager, this)
        val parts = MyUploadsPartsFragment()

        adapter.addAllFrags(parts)
        adapter.addAllTitles(PARTS)

        viewpager.adapter = adapter
        viewpager.offscreenPageLimit = 1
        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))

    }

    private fun setupTabs() {
        tabs.setupWithViewPager(viewpager)
        tabs.addOnTabSelectedListener(this)
    }

    private fun initFab() {

        fabPart.colorNormal = ContextCompat.getColor(this, R.color.colorPrimary)
        fabPart.colorPressed = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fabPart.colorRipple = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        fabPart.setImageDrawable(IconicsDrawable(this).icon(Ionicons.Icon.ion_plus).color(Color.WHITE).sizeDp(17))


        fabPart.setOnClickListener {
//            fam.hide(true)
            startActivity(Intent(this, AddPartActivity::class.java))
            AppUtils.animateEnterRight(this)
        }
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        viewpager.setCurrentItem(tab!!.position, true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> onBackPressed()
        }

        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        AppUtils.animateEnterLeft(this)
    }
}
