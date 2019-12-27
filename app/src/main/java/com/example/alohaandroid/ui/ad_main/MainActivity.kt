package com.example.alohaandroid.ui.ad_main


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.TelephonyManager
import android.transition.Transition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.example.alohaandroid.R

import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.ui.ae_chat.ChatFragment
import com.example.alohaandroid.ui.ae_phone.PhoneFragment
import com.example.alohaandroid.ui.favorite.FavoriteFragment
import com.example.alohaandroid.ui.ad_personal.PersonalFragment
import com.example.alohaandroid.ui.search.SearchActivity
import com.example.alohaandroid.utils.FadeInTransition
import com.example.alohaandroid.utils.FadeOutTransition
import com.example.alohaandroid.utils.SimpleTransitionListener
import com.example.alohaandroid.utils.extension.switchFragment
import com.example.alohaandroid.utils.linphone.LinphoneManager
import com.example.alohaandroid.utils.linphone.LinphonePreferences
import com.example.alohaandroid.utils.linphone.LinphoneService
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.include_toolbar_main.*
import org.linphone.core.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.fragment.app.Fragment
import com.example.alohaandroid.ui.ae_account.AccountFragment
import com.example.alohaandroid.ui.ae_statistical.StatisticalFragment


class MainActivity : BaseActivity() {

    private val contactFragment = PhoneFragment()
    private var mAccountCreator: AccountCreator? = null

    var deleteConversation: MenuItem? = null
    var editContact: MenuItem? = null
    var historyCall: MenuItem? = null
    var setting: MenuItem? = null

    private val navListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        var selectedFragment: Fragment? = null

        when (item.itemId) {
            R.id.navigation_contact -> selectedFragment = PhoneFragment()
            R.id.navigation_statistical -> selectedFragment = StatisticalFragment()
            R.id.navigation_chat -> selectedFragment = ChatFragment()
            R.id.navigation_user -> selectedFragment = AccountFragment()
        }

        if (selectedFragment != null) {
            switchFragment(selectedFragment, R.id.frameContent)
        }
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            navigationMain.selectedItemId = R.id.navigation_contact
            switchFragment(contactFragment, R.id.frameContent)
        }
        setSupportActionBar(toolbar)
//        main_toolbar.setOnClickListener(View.OnClickListener {
//
//            //            showKeyboard()
////            transitionToSearch()
//
//
//        })

        navigationMain.setOnNavigationItemSelectedListener(navListener)

        getAccountCreator()
        configureAccount()
    }


//    override fun onResume() {
//        super.onResume()
//        fadeToolbarIn()
//    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        deleteConversation = menu.findItem(R.id.delete_conversation)
        editContact = menu.findItem(R.id.edit_contact)
        historyCall = menu.findItem(R.id.history_call)
        setting = menu.findItem(R.id.setting)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                true
            }
            R.id.edit_contact -> {
                true
            }
            R.id.history_call -> {
                true
            }
            R.id.delete_conversation -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


//    fun checkFragment(fragment: Fragment) {
//        if (fragment is FavoriteFragment) {
////            appbar.visibility = View.VISIBLE
//            deleteConversation?.isVisible = false
//            editContact?.isVisible = true
//            historyCall?.isVisible = true
//            setting?.isVisible = true
////            main_toolbar.title = "Tìm kiếm liên hệ"
////            main_toolbar.setOnClickListener(View.OnClickListener {
////
////                showKeyboard()
////                transitionToSearch()
////
////
////            })
//        }
//        if (fragment is PhoneFragment) {
////            appbar.visibility = View.VISIBLE
//            deleteConversation?.isVisible = false
//            editContact?.isVisible = true
//            historyCall?.isVisible = true
//            setting?.isVisible = true
////            main_toolbar.title = "Tìm kiếm liên hệ"
////            main_toolbar.setOnClickListener(View.OnClickListener {
////
////                showKeyboard()
////                transitionToSearch()
////
////
////            })
//
//        }
//        if (fragment is ChatFragment) {
////            appbar.visibility = View.VISIBLE
//            editContact?.isVisible = false
//            historyCall?.isVisible = false
//            deleteConversation?.isVisible = true
//            setting?.isVisible = true
////            main_toolbar.title = "Tìm kiếm Tin Nhắn"
//
//        }
//        if (fragment is PersonalFragment) {
////            appbar.visibility = View.GONE
//        }
//    }


//    private fun transitionToSearch() {
//        val transition = FadeOutTransition.withAction(navigateToSearchWhenDone())
//
//        TransitionManager.beginDelayedTransition(main_toolbar, transition)
//        main_toolbar.hideContent()
//    }

//    private fun navigateToSearchWhenDone(): Transition.TransitionListener {
//        return object : SimpleTransitionListener() {
//            override fun onTransitionEnd(transition: Transition) {
//
//                SearchActivity.startActivity(this@MainActivity)
//
//                overridePendingTransition(0, 0)
//
//            }
//        }
//    }

//    private fun fadeToolbarIn() {
//        TransitionManager.beginDelayedTransition(main_toolbar, FadeInTransition.createTransition())
//        main_toolbar.showContent()
//    }
//
//    protected fun showKeyboard() {
//        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    fun getAccountCreator() {
        if (mAccountCreator == null) {
            val url = LinphonePreferences.instance().getXmlrpcUrl()
            val core = LinphoneManager.getCore()
            core.loadConfigFromXml(LinphonePreferences.instance().getDefaultDynamicConfigFile())
            mAccountCreator = core.createAccountCreator(url)

        }
    }

    fun configureAccount() {
        mAccountCreator?.username = "2000"
        mAccountCreator?.domain = "192.168.1.48:6000"
        mAccountCreator?.password = "12345678"
        mAccountCreator?.displayName = "nam"

        mAccountCreator?.setTransport(TransportType.Udp)
//        mAccountCreator?.setTransport(TransportType.Tcp)
        //  mAccountCreator?.setTransport(TransportType.Tls)

        createProxyConfigAndLeaveAssistant()
    }

    fun createProxyConfigAndLeaveAssistant() {
        val core = LinphoneManager.getCore()
        val useLinphoneDefaultValues =
            getString(R.string.default_domain).equals(mAccountCreator?.getDomain())
        if (useLinphoneDefaultValues) {
            core.loadConfigFromXml(LinphonePreferences.instance().linphoneDynamicConfigFile)
        }

        val proxyConfig = mAccountCreator?.createProxyConfig()


        if (useLinphoneDefaultValues) {
            // Restore default values
            core.loadConfigFromXml(LinphonePreferences.instance().defaultDynamicConfigFile)
        } else {
            // If this isn't a sip.linphone.org account, disable push notifications and enable
            // service notification, otherwise incoming calls won't work (most probably)
            LinphonePreferences.instance().serviceNotificationVisibility = true
            LinphoneService.instance().getNotificationManager().startForeground()
        }

        if (proxyConfig == null) {
            // TODO: display error message
        } else {
            if (proxyConfig.dialPrefix == null) {
                val dialPlan = getDialPlanForCurrentCountry()
                if (dialPlan != null) {
                    proxyConfig.dialPrefix = dialPlan.getCountryCallingCode()
                }
            }
            proxyConfig.edit();
            proxyConfig.dialPrefix = ""
            proxyConfig.done()
            LinphonePreferences.instance().firstLaunchSuccessful()
        }
    }

    fun getDialPlanForCurrentCountry(): DialPlan? {
        try {
            val tm = getSystemService(TELEPHONY_SERVICE) as TelephonyManager
            val countryIso = tm.networkCountryIso
            return getDialPlanFromCountryCode(countryIso)
        } catch (e: Exception) {
        }

        return null
    }


    private fun getDialPlanFromCountryCode(countryCode: String?): DialPlan? {
        if (countryCode == null || countryCode.isEmpty()) return null

        for (c in Factory.instance().dialPlans) {
            if (countryCode.equals(c.isoCountryCode, ignoreCase = true)) return c
        }
        return null
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, MainActivity::class.java))
        }
    }

}
