package com.example.alohaandroid.ui.ae_account

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.LogoutViewModel
import com.example.alohaandroid.ui.ab_login.LoginActivity
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.switchFragment
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseActivity(), View.OnClickListener {

    private var logoutViewModel: LogoutViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        initLogOutViewModel()

        imBack.setOnClickListener(this)
        tvTermsOfService.setOnClickListener(this)
        tvConnected.setOnClickListener(this)
        tvTutorial.setOnClickListener(this)
        tvLogout.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imBack -> {
                onBackPressed()
            }
            R.id.tvTermsOfService -> {
                val termsOfServiceFragment = TermsOfServiceFragment()
                switchFragment(termsOfServiceFragment, R.id.fl)
            }
            R.id.tvConnected -> {

            }
            R.id.tvTutorial -> {
                val userManualFragment = UserManualFragment()
                switchFragment(userManualFragment, R.id.fl)
            }
            R.id.tvLogout -> {
                val builder1: AlertDialog.Builder = AlertDialog.Builder(this)
                builder1.setTitle(R.string.dang_xuat)
                builder1.setMessage(R.string.ban_co_muon_dang_xuat)
                builder1.setPositiveButton(
                    R.string.dang_xuat
                ) { dialogInterface, i ->
                    logoutViewModel?.LogOut()
                    dialogInterface.dismiss()
                }
                builder1.setNegativeButton(
                    R.string.khong
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                val alertDialog = builder1.create()
                alertDialog.show()

            }
        }
    }

    fun initLogOutViewModel() {
            logoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel::class.java).apply {
                Success.observe(this@SettingActivity,
                    Observer {
                        it?.let {
                            if (it) {
                                startActivity(Intent(this@SettingActivity, LoginActivity::class.java))
                                finish()
                                SharePrefs().getInstance().clear()

                            }
                        }
                    })
                Failr.observe(this@SettingActivity, Observer {

                })
                loadingStatus.observe(this@SettingActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
    }
}
