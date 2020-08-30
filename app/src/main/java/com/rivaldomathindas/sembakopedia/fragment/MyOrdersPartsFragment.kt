package com.rivaldomathindas.sembakopedia.fragment


import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.google.firebase.database.*
import com.rivaldomathindas.sembakopedia.adapter.PartOrdersAdapter
import com.rivaldomathindas.sembakopedia.model.PartOrder
import com.rivaldomathindas.sembakopedia.network.BaseFragment
import com.rivaldomathindas.sembakopedia.utils.K
import com.rivaldomathindas.sembakopedia.utils.RecyclerFormatter
import com.rivaldomathindas.sembakopedia.utils.hideView
import com.rivaldomathindas.sembakopedia.utils.showView
import com.rivaldomathindas.sembakopedia.R
import kotlinx.android.synthetic.main.fragment_my_parts_orders.*
import timber.log.Timber

class MyOrdersPartsFragment : BaseFragment() {
    private lateinit var partOrdersAdapter: PartOrdersAdapter
    private lateinit var ordersQuery: Query

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        ordersQuery = getDatabaseReference().child(K.REQUESTS).child(getUid())
        return inflater.inflate(R.layout.fragment_my_parts_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)

        ordersQuery.addValueEventListener(carsValueListener)
        ordersQuery.addChildEventListener(carsChildListener)
    }

    private fun initViews(v: View) {
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(activity)
        rv.itemAnimator = DefaultItemAnimator()
        rv.addItemDecoration(RecyclerFormatter.SimpleDividerItemDecoration(requireActivity()))

        partOrdersAdapter = PartOrdersAdapter(requireActivity())
        rv.adapter = partOrdersAdapter
        rv.showShimmerAdapter()

    }


    private val carsValueListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {
            Timber.e("Error fetching chats: $p0")
            noCars()
        }

        override fun onDataChange(p0: DataSnapshot) {
            if (p0.exists()) {
                hasCars()
            } else {
                noCars()
            }
        }
    }

    private val carsChildListener= object : ChildEventListener {

        override fun onCancelled(p0: DatabaseError) {
            Timber.e("Child listener cancelled: $p0")
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            Timber.e("Chat moved: ${p0.key}")
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//            val booking = p0.getValue(Booking::class.java)
//            bookingsAdapter.addBooking(booking!!)
        }

        override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            val order = p0.getValue(PartOrder::class.java)
            partOrdersAdapter.addPartOrder(order!!)
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            Timber.e("Chat removed: ${p0.key}")
        }
    }

    private fun hasCars() {
        rv?.hideShimmerAdapter()
        empty?.hideView()
        rv?.showView()
    }

    private fun noCars() {
        rv?.hideShimmerAdapter()
        rv?.hideView()
        empty?.showView()
    }


    override fun onDestroy() {
        super.onDestroy()
        ordersQuery.removeEventListener(carsValueListener)
        ordersQuery.removeEventListener(carsChildListener)
    }

}
