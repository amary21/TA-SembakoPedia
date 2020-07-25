package com.rivaldomathindas.sembakopedia.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.fragment.FirstFragment
import com.rivaldomathindas.sembakopedia.fragment.SecondFragment
import com.rivaldomathindas.sembakopedia.fragment.ThirdFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addNavigationView()
        navigationView.show(1)
        openFragment(FirstFragment())
        onClickNavigationView()
    }

    private fun onClickNavigationView() {
        navigationView.setOnClickMenuListener {
            when (it.id) {
                1 -> {
                    val firstFragment =
                        FirstFragment()
                    openFragment(firstFragment)
                    return@setOnClickMenuListener
                }
                2 -> {
                    val secondFragment =
                        SecondFragment()
                    openFragment(secondFragment)
                    return@setOnClickMenuListener
                }
                3 -> {
                    val thirdFragment =
                        ThirdFragment()
                    openFragment(thirdFragment)
                    return@setOnClickMenuListener
                }
            }
        }
    }

    private fun addNavigationView() {
        navigationView.add(MeowBottomNavigation.Model(1,
            R.drawable.ic_home
        ))
        navigationView.add(MeowBottomNavigation.Model(2,
            R.drawable.ic_statistics
        ))
        navigationView.add(MeowBottomNavigation.Model(3,
            R.drawable.ic_settings
        ))
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
