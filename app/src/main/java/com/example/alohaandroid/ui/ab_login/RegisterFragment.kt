package com.example.alohaandroid.ui.ab_login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.*
import com.example.alohaandroid.ui.ab_register.RegisterActivity
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.utils.extension.showToast
import com.example.alohaandroid.utils.extension.switchFragment
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : BaseFragment(), View.OnClickListener {

    private var registerUserViewModel: RegisterUserViewModel? = null
    private var checkExistEmailViewModel: CheckExistEmailViewModel? = null
    private var checkExistPhoneViewModel: CheckExistPhoneViewModel? = null
    private var checkExistUsernameViewModel: CheckExistUsernameViewModel? = null
    private lateinit var signInViewModel: SignInViewModel

    private var idRegister: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModelregisterUser()
        initViewModelCheckExistEmail()
        initViewModelCheckExistPhone()
//        initViewModelCheckExistUsername()
        idRegister = arguments?.getInt(RegisterActivity.EXTRA_ID) ?: 0
        btnRegister.setOnClickListener(this)
        tvLogin.setOnClickListener(this)
        imBack.setOnClickListener(this)
        llLogin.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.llLogin -> {
                closeKeyboard()
            }
            R.id.imBack -> {
                activity?.onBackPressed()
            }
            R.id.tvLogin -> {

                if (idRegister == 1) {
                    startActivity(Intent(activity, LoginActivity::class.java))
                } else {
                    activity?.onBackPressed()
                }
            }

            R.id.btnRegister -> {

                signInViewModel =
                    ViewModelProviders.of(this).get(SignInViewModel::class.java).apply {
                        loginSuccess.observe(this@RegisterFragment, Observer {
                        })

                    }
                signInViewModel.loginWithApp("12345678", "aloha", 0)

                if (edtName.text!!.isEmpty() || edtPhoneNumber.text!!.isEmpty() || edtEmail.text!!.isEmpty()) {
                    tvErrorAccount.visibility = View.VISIBLE
                }else{
                    if (rbAgreeToTermsAndConditions.isChecked) {
                        checkExistPhoneViewModel?.checkExistPhone(edtPhoneNumber.text.toString())
                    }else {
                        showToast(R.string.ban_chua_dong_y_voi_dieu_khoan_va_dieu_kien_su_dung)
                    }
                }
            }
        }
    }

    private fun initViewModelregisterUser() {
        activity?.let {
            registerUserViewModel =
                ViewModelProviders.of(this).get(RegisterUserViewModel::class.java).apply {
                    registerSuccess.observe(this@RegisterFragment, Observer {
                        it?.let {
                            if (it == 1) {
                                val verifyAccountFragment = VerifyAccountFragment()
                                switchFragment(verifyAccountFragment, R.id.fl)
                            }
                        }
                    })

                    message.observe(this@RegisterFragment, Observer {
                        it?.let {
                            //                            showToast(it)
                        }
                    })
                    loadingStatus.observe(this@RegisterFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }

    private fun initViewModelCheckExistEmail() {
        activity?.let {
            checkExistEmailViewModel =
                ViewModelProviders.of(this).get(CheckExistEmailViewModel::class.java).apply {
                    emailExist.observe(this@RegisterFragment, Observer {
                        it?.let {
                            if (!it) {
                                if (edtPass.text.toString() == edtRePassword.text.toString()) {
                                    registerUserViewModel?.register(
                                        edtEmail.text.toString(),
                                        edtPass.text.toString(),
                                        edtName.text.toString(),
                                        edtPhoneNumber.text.toString(),
                                        "",
                                        "",
                                        "",
                                        ""
                                    )
                                } else {
                                    tvRePassword.error = getString(R.string.error_incorrect_pasword)
                                }
                            } else {
                                Toast.makeText(
                                    activity,
                                    R.string.email_da_ton_tai,
                                    Toast.LENGTH_LONG
                                ).show()

                            }
                        }
                    })

                    message.observe(this@RegisterFragment, Observer {
                        it?.let {
                            //                                                        showToast(it)
                        }
                    })
                    loadingStatus.observe(this@RegisterFragment, Observer {
                        showOrHideProgressDialog(it)
                    })

                }
        }
    }

    private fun initViewModelCheckExistPhone() {
        activity?.let {
            checkExistPhoneViewModel =
                ViewModelProviders.of(this).get(CheckExistPhoneViewModel::class.java).apply {
                    phoneExist.observe(this@RegisterFragment, Observer {
                        it?.let {
                            if (!it) {
                                checkExistEmailViewModel?.checkExistEmail(edtEmail.text.toString())
                            } else {
                                Toast.makeText(
                                    activity,
                                    R.string.so_dien_thoai_da_ton_tai,
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    })

                    message.observe(this@RegisterFragment, Observer {
                        it?.let {
                            //                                                        showToast(it)
                        }
                    })
                    loadingStatus.observe(this@RegisterFragment, Observer {
                        showOrHideProgressDialog(it)
                    })

                }
        }
    }

    private fun initViewModelCheckExistUsername() {
        activity?.let {
            checkExistUsernameViewModel =
                ViewModelProviders.of(this).get(CheckExistUsernameViewModel::class.java).apply {
                    usernameExist.observe(this@RegisterFragment, Observer {
                        it?.let {
                            if (!it) {

                            }
                        }
                    })

                    message.observe(this@RegisterFragment, Observer {
                        it?.let {
                            //                                                        showToast(it)
                        }
                    })
                    loadingStatus.observe(this@RegisterFragment, Observer {
                        showOrHideProgressDialog(it)
                    })

                }
        }
    }

    private fun closeKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
