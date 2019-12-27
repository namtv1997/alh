package com.example.alohaandroid.ui.forgot_password

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.ForgotPasswordViewModel
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_forgot_password.*


class ForgotPasswordFragment : BaseFragment(), View.OnClickListener {

    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        btnSendCode.setOnClickListener(this)
        initViewModelforgotPassword()
    }

   private fun initViewModelforgotPassword() {
        activity?.let {
            forgotPasswordViewModel =
                ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java).apply {
                    password.observe(this@ForgotPasswordFragment, Observer {
                        it?.let {
                            if (it == 1) {
                                val bundle = Bundle()
                                bundle.putInt(IS_FORGOT_FRAGMENT,1)
//                                val verifyFragment =
//                                    VerifyFragment()
//                                verifyFragment.arguments=bundle
//                                switchFragment(verifyFragment, R.id.frameContent)
                            }
                        }
                    })

                    message.observe(this@ForgotPasswordFragment, Observer {
                        it?.let {
                            showToast(it)
                        }
                    })
                    loadingStatus.observe(this@ForgotPasswordFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSendCode -> {
                forgotPasswordViewModel.forgotPassword(
                    edtName.text.toString(),
                    edtPhoneNumber.text.toString()
                )
            }
        }
    }

    companion object {
        const val IS_FORGOT_FRAGMENT = "IS_FORGOT_FRAGMENT"
    }

}
