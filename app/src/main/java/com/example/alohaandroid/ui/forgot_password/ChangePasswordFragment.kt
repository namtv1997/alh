package com.example.alohaandroid.ui.forgot_password


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.change_password.ChangePasswordViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_change_password.*


class ChangePasswordFragment : BaseFragment(),View.OnClickListener {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    private val uidRegister =  SharePrefs().getInstance()[Common.UID_REGISTER, String::class.java]

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initchangePasswordViewModel()
        btnChangePassword.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnChangePassword->{
                if (uidRegister != null) {
                    changePasswordViewModel.changePassword(uidRegister,edtReNewPassword.text.toString())
                }
            }
        }
    }

    fun initchangePasswordViewModel() {
        activity.let {
            changePasswordViewModel =
                ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java).apply {
                    changePassword.observe(this@ChangePasswordFragment, Observer {
                        if (it) {
                            val bundle = Bundle()
                            bundle.putInt(ForgotPasswordFragment.IS_FORGOT_FRAGMENT,1)
//                            val doneVerifyFragment =
//                                DoneVerifyFragment()
//                            doneVerifyFragment.arguments=bundle
//                            switchFragment(doneVerifyFragment, R.id.frameContent)
                        }

                    })
                    message.observe(this@ChangePasswordFragment, Observer {
                        it?.let {
                            showToast(it)
                        }
                    })
                    loadingStatus.observe(this@ChangePasswordFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }


}
