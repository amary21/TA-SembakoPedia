package com.rivaldomathindas.sembakopedia.activity

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.labters.lottiealertdialoglibrary.ClickListener
import com.labters.lottiealertdialoglibrary.DialogTypes
import com.labters.lottiealertdialoglibrary.LottieAlertDialog
import com.mikepenz.ionicons_typeface_library.Ionicons
import com.mikepenz.materialdrawer.AccountHeaderBuilder
import com.mikepenz.materialdrawer.Drawer
import com.mikepenz.materialdrawer.DrawerBuilder
import com.mikepenz.materialdrawer.model.DividerDrawerItem
import com.mikepenz.materialdrawer.model.ProfileDrawerItem
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem
import com.rivaldomathindas.sembakopedia.R
import com.rivaldomathindas.sembakopedia.fragment.FirstFragment
import com.rivaldomathindas.sembakopedia.fragment.SecondFragment
import com.rivaldomathindas.sembakopedia.fragment.ThirdFragment
import com.rivaldomathindas.sembakopedia.network.BaseActivity
import com.rivaldomathindas.sembakopedia.utils.AppUtils
import com.rivaldomathindas.sembakopedia.utils.K
import com.rivaldomathindas.sembakopedia.utils.PreferenceHelper
import com.rivaldomathindas.sembakopedia.utils.PreferenceHelper.get
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : BaseActivity() {

    private var doubleBackToExit = false

    private lateinit var drawer: Drawer
    private lateinit var prefs: SharedPreferences
    private lateinit var alertDialog : LottieAlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = PreferenceHelper.defaultPrefs(this@MainActivity)

        addNavigationView()
        navigationView.show(1)
        openFragment(FirstFragment())
        setupDrawer()
        setupBottomNavigation()
    }

    private fun setupDrawer() {
        val accountHeader = AccountHeaderBuilder().withActivity(this)
            .withSelectionListEnabled(false)
            .withHeaderBackground(R.drawable.ic_launcher_background)
            .addProfiles(
                ProfileDrawerItem()
                .withName(prefs[K.NAME, ""])
                .withEmail(prefs[K.EMAIL, ""])
                .withIcon(R.drawable.person))
            .build()

        val home = SecondaryDrawerItem().withIdentifier(0).withName("Home").withIcon(R.drawable.home)
        val market = SecondaryDrawerItem().withIdentifier(1).withName("My Market").withIcon(R.drawable.icon_alamat)
        val statistics = SecondaryDrawerItem().withIdentifier(2).withName("Statistics").withIcon(R.drawable.statistics)
        val chat = SecondaryDrawerItem().withIdentifier(3).withName("Chats").withIcon(R.drawable.icon_komentar)
        val settings = SecondaryDrawerItem().withIdentifier(4).withName("Settings").withIcon(R.drawable.gear)
        val about = SecondaryDrawerItem().withIdentifier(5).withName("About").withIcon(Ionicons.Icon.ion_android_contacts)
        val exit = SecondaryDrawerItem().withIdentifier(6).withName("Logout").withIcon(Ionicons.Icon.ion_log_out)

        drawer = DrawerBuilder().withActivity(this)
            .withToolbar(toolbar)
            .withAccountHeader(accountHeader)
            .addDrawerItems(home, market, statistics , chat, DividerDrawerItem(), settings, about, exit)
            .withOnDrawerItemClickListener { _, _, drawerItem ->
                when(drawerItem) {
                    home -> {
                        openFragment(FirstFragment())
                        navigationView.show(1)
                    }
                    market -> launchActivity(MyUploadsActivity::class.java)
                    statistics -> {
                        openFragment(SecondFragment())
                        navigationView.show(2)
                    }
                    chat -> {
                        openFragment(ThirdFragment())
                        navigationView.show(3)
                    }
                    about -> toast("Clicked About")
                    settings -> toast("Clicked Settings")
                    exit -> logOut()
                }
                true
            }
            .build()
    }

    private fun setupBottomNavigation() {
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
            R.drawable.ic_baseline_chat_24
        ))
    }

    private fun logOut() {
        alertDialog  = LottieAlertDialog.Builder(this, DialogTypes.TYPE_LOADING)
            .setTitle("Loading")
            .setDescription("Please Wait")
            .build()
        alertDialog.setCancelable(false)
        alertDialog.show()

        Handler().postDelayed(Runnable {
            alertDialog.changeDialog(
                LottieAlertDialog.Builder(this, DialogTypes.TYPE_QUESTION)
                .setTitle("Log Out")
                .setDescription("Are you sure to log out ?")
                .setPositiveText("Cancel")
                .setNegativeText("Yes")
                .setPositiveButtonColor(Color.parseColor("#ffd15c"))
                .setPositiveTextColor(Color.parseColor("#ffeaea"))
                .setNegativeButtonColor(Color.parseColor("#f05540"))
                .setNegativeTextColor(Color.parseColor("#ffeaea"))
                .setPositiveListener(object: ClickListener {
                    override fun onClick(dialog: LottieAlertDialog) {
                        dialog.dismiss()
                    }
                })
                .setNegativeListener(object : ClickListener
                {
                    override fun onClick(dialog: LottieAlertDialog) {
                        val firebaseAuth = FirebaseAuth.getInstance()
                        firebaseAuth.signOut()
                        FirebaseMessaging.getInstance()
                            .unsubscribeFromTopic(K.TOPIC_GLOBAL)
                        startActivity(Intent(this@MainActivity, AuthActivity::class.java))
                        AppUtils.animateEnterLeft(this@MainActivity)
                        finish()
                    }
                })
            )
        },2000)
    }

    private fun launchActivity(intentClass: Class<*>) {
        val intent = Intent(this, intentClass)
        startActivity(intent)
        overridePendingTransition(R.anim.enter_b, R.anim.exit_a)

        Handler().postDelayed({
            drawer.closeDrawer()
            drawer.setSelection(0)
        }, 300)

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }


    override fun onBackPressed() {
        if (doubleBackToExit) {
            super.finish()
        } else {
            doubleBackToExit = true
            toast("Tap back again to exit")

            Handler().postDelayed({doubleBackToExit = false}, 1500)
        }
    }
}
