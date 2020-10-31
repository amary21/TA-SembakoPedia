package com.rivaldomathindas.sembakopedia.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.model.DetailProduct
import com.rivaldomathindas.sembakopedia.utils.K
import com.rivaldomathindas.sembakopedia.utils.K.STATISTIC_PRODUCT
import com.rivaldomathindas.sembakopedia.utils.TimeFormatter

class StatsWeeklyFragment : Fragment() {
    private var paramStats: DetailProduct? = null

    companion object {
        @JvmStatic
        fun newInstance(paramStats: DetailProduct) =
            StatsWeeklyFragment().apply {
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
        return inflater.inflate(R.layout.fragment_stats_weekly, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }
}