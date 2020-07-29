package com.rivaldomathindas.sembakopedia.fragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.activity.AuthActivity
import com.rivaldomathindas.sembakopedia.utils.AppUtils
import com.rivaldomathindas.sembakopedia.utils.K
import kotlinx.android.synthetic.main.fragment_third.*

class ThirdFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_third, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        button.setOnClickListener {
            Toast.makeText(activity, "Test", Toast.LENGTH_SHORT).show()
        }

//           fun Context.makeDialog() = alert("Are you sure you want to log out?") {
//                title = "Log out"
//                positiveButton("LOG OUT") {
//                    val firebaseAuth = FirebaseAuth.getInstance()
//                    firebaseAuth.signOut()
//                    FirebaseMessaging.getInstance().unsubscribeFromTopic(K.TOPIC_GLOBAL)
//
//                    startActivity(Intent(activity, AuthActivity::class.java))
//                    AppUtils.animateEnterLeft(requireActivity())
//                }
//                negativeButton("CANCEL") {}
//            }.show()
        }


    }

