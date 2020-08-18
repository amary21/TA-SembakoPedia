package com.rivaldomathindas.sembakopedia.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.activity.PartActivity
import com.rivaldomathindas.sembakopedia.adapter.PartsAdapter
import com.rivaldomathindas.sembakopedia.callbacks.PartCallback
import com.rivaldomathindas.sembakopedia.model.Part
import com.rivaldomathindas.sembakopedia.network.BaseFragment
import com.rivaldomathindas.sembakopedia.utils.*
import kotlinx.android.synthetic.main.fragment_parts.*
import timber.log.Timber

class FirstFragment : BaseFragment(), PartCallback {
    private lateinit var partsAdapter: PartsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_parts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()

        loadParts()
    }

    private fun initViews() {
        rv.setHasFixedSize(true)
        rv.layoutManager = GridLayoutManager(requireActivity(), 2)
        rv.addItemDecoration(RecyclerFormatter.GridItemDecoration(requireActivity(), 2, 10))
        rv.itemAnimator = DefaultItemAnimator()
        (rv.itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false

        partsAdapter = PartsAdapter(this)
        rv.adapter = partsAdapter

    }

    private fun loadParts() {
        getFirestore().collection(K.PARTS)
            .orderBy(K.TIMESTAMP, Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Timber.e("Error fetching parts $firebaseFirestoreException")
                    noParts()
                }

                if (querySnapshot == null || querySnapshot.isEmpty) {
                    noParts()
                } else {
                    hasParts()

                    for (docChange in querySnapshot.documentChanges) {

                        when(docChange.type) {
                            DocumentChange.Type.ADDED -> {
                                val part = docChange.document.toObject(Part::class.java)
                                partsAdapter.addPart(part)
                            }

                            DocumentChange.Type.MODIFIED -> {
                                val part = docChange.document.toObject(Part::class.java)
                                partsAdapter.updatePart(part)
                            }

                            DocumentChange.Type.REMOVED -> {
                                val part = docChange.document.toObject(Part::class.java)
                                partsAdapter.removePart(part)
                            }

                        }

                    }

                }
            }
    }

    private fun hasParts() {
        rv?.hideShimmerAdapter()
        empty?.hideView()
        rv?.showView()
    }

    private fun noParts() {
        rv?.hideShimmerAdapter()
        rv?.hideView()
        empty?.showView()
    }

    override fun onClick(v: View, part: Part) {
        val i = Intent(activity, PartActivity::class.java)
        i.putExtra(K.PART, part)
        i.putExtra(K.MINE, (part.sellerId == getUid()))
        startActivity(i)
        AppUtils.animateFadein(requireActivity())

    }
}
