package com.example.alohaandroid.ui.ad_personal


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.alohaandroid.R
import com.example.alohaandroid.domain.remote.pojo.response.InforUser
import com.example.alohaandroid.ui.a_viewmodel.GetUserViewModel
import com.example.alohaandroid.ui.a_viewmodel.LogoutViewModel
import com.example.alohaandroid.ui.ab_login.LoginActivity
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.change_password.ChangePasswordActivity
import com.example.alohaandroid.ui.ad_main.MainActivity
import com.example.alohaandroid.utils.extension.SharePrefs
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_personal.*

class PersonalFragment : BaseFragment(), View.OnClickListener {

    private lateinit var getUserViewModel: GetUserViewModel
    private var logoutViewModel: LogoutViewModel? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        (activity as MainActivity).checkFragment(this)
        btnChangePassword.setOnClickListener(this)
        btnLogout.setOnClickListener(this)
        initLogOutViewModel()
        initUserViewModel()
        getUserViewModel.getuserInfor()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnChangePassword -> {
                startActivity(Intent(activity, ChangePasswordActivity::class.java))
            }
            R.id.btnLogout -> {
                val builder1: AlertDialog.Builder = AlertDialog.Builder(activity!!)
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
        activity?.let {
            logoutViewModel = ViewModelProviders.of(this).get(LogoutViewModel::class.java).apply {
                Success.observe(this@PersonalFragment,
                    Observer {
                        it?.let {
                            if (it) {
                                startActivity(Intent(activity, LoginActivity::class.java))
                                activity!!.finish()
                                SharePrefs().getInstance().clear()

                            }
                        }
                    })
                Failr.observe(this@PersonalFragment, Observer {

                })
                loadingStatus.observe(this@PersonalFragment, Observer {
                    showOrHideProgressDialog(it)
                })
            }
        }
    }

    fun initUserViewModel() {
        activity?.let {
            getUserViewModel =
                ViewModelProviders.of(this).get(GetUserViewModel::class.java).apply {
                    userInfor.observe(this@PersonalFragment, Observer {
                        it?.let {
                            getDataUser(it)
                        }
                    })

                    loadingStatus.observe(this@PersonalFragment, Observer {
                        showOrHideProgressDialog(it)
                    })

                }


        }
    }

    fun getDataUser(data: InforUser) {
        tvName.text = data.fullName
        tvNumBer.text = data.phone
        tvTotalMoney.text = data.totalMoney.toString()
        Glide.with(this).load(data.avatar).placeholder(R.drawable.iv_call).into(ivAvatar)
    }

}
