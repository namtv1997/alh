package com.example.alohaandroid.ui.ab_login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.ForgotPasswordViewModel
import com.example.alohaandroid.ui.a_viewmodel.ValidateVerifyCodeViewModel
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.ui.change_password.ChangePasswordViewModel
import com.example.alohaandroid.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_forgot_password2.*
import kotlinx.android.synthetic.main.fragment_forgot_password2.btnConfirm
import kotlinx.android.synthetic.main.fragment_forgot_password2.etNumberVerify1
import kotlinx.android.synthetic.main.fragment_forgot_password2.etNumberVerify2
import kotlinx.android.synthetic.main.fragment_forgot_password2.etNumberVerify3
import kotlinx.android.synthetic.main.fragment_forgot_password2.etNumberVerify4
import kotlinx.android.synthetic.main.fragment_forgot_password2.etNumberVerify5
import kotlinx.android.synthetic.main.fragment_forgot_password2.etNumberVerify6
import kotlinx.android.synthetic.main.fragment_forgot_password2.ll
import kotlinx.android.synthetic.main.fragment_forgot_password2.lnNumber
import kotlinx.android.synthetic.main.fragment_forgot_password2.tvCountSeconds
import kotlinx.android.synthetic.main.fragment_forgot_password2.tvSentAgain

/**
 * A simple [Fragment] subclass.
 */
class ForgotPasswordFragment : BaseFragment(), View.OnClickListener {

    private var confirm = 0
    private var uid: String = ""
    private lateinit var yourCountDownTimer: CountDownTimer
    private var counter: Int = 61
    private val START_TIME_IN_MILLIS: Long = 61000
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private lateinit var forgotPasswordViewModel: ForgotPasswordViewModel
    private var validateVerifyCodeViewModel: ValidateVerifyCodeViewModel? = null
    private lateinit var changePasswordViewModel: ChangePasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgot_password2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModelforgotPassword()
        initViewModelValidateVerifyCode()
        initchangePasswordViewModel()

        btnConfirm.setOnClickListener(this)
        ll.setOnClickListener(this)
        tvSentAgain.setOnClickListener(this)
        btnChangePassword.setOnClickListener(this)
        btnReturnLogin.setOnClickListener(this)
        etNumberVerify1.setOnClickListener(this)
        etNumberVerify2.setOnClickListener(this)
        etNumberVerify3.setOnClickListener(this)
        etNumberVerify4.setOnClickListener(this)
        etNumberVerify5.setOnClickListener(this)
        etNumberVerify6.setOnClickListener(this)

        etNumberVerify1.addTextChangedListener(TextWatcher)
        etNumberVerify2.addTextChangedListener(TextWatcher)
        etNumberVerify3.addTextChangedListener(TextWatcher)
        etNumberVerify4.addTextChangedListener(TextWatcher)
        etNumberVerify5.addTextChangedListener(TextWatcher)
        etNumberVerify6.addTextChangedListener(TextWatcher)
    }

    private val TextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(
            s: CharSequence, start: Int, count: Int, after: Int
        ) {


        }

        override fun onTextChanged(
            s: CharSequence, start: Int, before: Int, count: Int
        ) {
            if (etNumberVerify1.length() == 1) {
                etNumberVerify2.requestFocus()
            }
            if (etNumberVerify2.length() == 1) {
                etNumberVerify3.requestFocus()
            }
            if (etNumberVerify3.length() == 1) {
                etNumberVerify4.requestFocus()
            }
            if (etNumberVerify4.length() == 1) {
                etNumberVerify5.requestFocus()
            }
            if (etNumberVerify5.length() == 1) {
                etNumberVerify6.requestFocus()
            }
            if (etNumberVerify6.length() == 1) {
                ll.performClick()
            }
        }

        override fun afterTextChanged(s: Editable) {

        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ll -> {
                closeKeyboard()
            }
            R.id.btnConfirm -> {
                if (confirm == 0) {
                    if (edtName.text!!.isEmpty() || edtPhoneNumber.text!!.isEmpty()) {
                        Toast.makeText(
                            activity,
                            R.string.chua_nhap_email_hoac_so_dien_thoai,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        forgotPasswordViewModel.forgotPassword(
                            edtName.text.toString(),
                            edtPhoneNumber.text.toString()
                        )
                    }
                } else {
                    if (etNumberVerify1.length() == 0 || etNumberVerify2.length() == 0 || etNumberVerify3.length() == 0 || etNumberVerify4.length() == 0 || etNumberVerify5.length() == 0 || etNumberVerify6.length() == 0) {
                        Toast.makeText(activity, R.string.chua_nhap_day_du_ma_xac_thuc, Toast.LENGTH_LONG)
                            .show()
                    } else {
                        val code: String =
                            etNumberVerify1.text.toString() + etNumberVerify2.text.toString() + etNumberVerify3.text.toString() + etNumberVerify4.text.toString() + etNumberVerify5.text.toString() + etNumberVerify6.text.toString()
                        validateVerifyCodeViewModel?.verifyCode(code)
                    }
                }
            }
            R.id.etNumberVerify1 -> {
                etNumberVerify1.setText("")
                etNumberVerify2.setText("")
                etNumberVerify3.setText("")
                etNumberVerify4.setText("")
                etNumberVerify5.setText("")
                etNumberVerify6.setText("")
            }
            R.id.etNumberVerify2 -> {
                etNumberVerify2.setText("")
                etNumberVerify3.setText("")
                etNumberVerify4.setText("")
                etNumberVerify5.setText("")
                etNumberVerify6.setText("")
            }
            R.id.etNumberVerify3 -> {
                etNumberVerify3.setText("")
                etNumberVerify4.setText("")
                etNumberVerify5.setText("")
                etNumberVerify6.setText("")
            }
            R.id.etNumberVerify4 -> {
                etNumberVerify4.setText("")
                etNumberVerify5.setText("")
                etNumberVerify6.setText("")
            }
            R.id.etNumberVerify5 -> {
                etNumberVerify5.setText("")
                etNumberVerify6.setText("")
            }
            R.id.etNumberVerify6 -> {
                etNumberVerify6.setText("")
            }

            R.id.tvSentAgain -> {
                forgotPasswordViewModel.forgotPassword(
                    edtName.text.toString(),
                    edtPhoneNumber.text.toString()
                )
                counter = 61
                mTimeLeftInMillis = START_TIME_IN_MILLIS
            }
            R.id.btnChangePassword -> {
                if (edtPass.text!!.isEmpty() || edtRePassword.text!!.isEmpty()) {
                    Toast.makeText(
                        activity,
                        R.string.ban_chua_nhap_day_du_thong_tin,
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    if (edtPass.text.toString() == edtRePassword.text.toString()) {
                        changePasswordViewModel.changePassword(uid, edtPass.text.toString())
                    } else {
                        Toast.makeText(
                            activity,
                            R.string.mat_khau_khong_trung_khop,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            R.id.btnReturnLogin -> {
                startActivity(Intent(activity, LoginActivity::class.java))
                activity?.finish()
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

    private fun initViewModelforgotPassword() {
        activity?.let {
            forgotPasswordViewModel =
                ViewModelProviders.of(this).get(ForgotPasswordViewModel::class.java).apply {
                    password.observe(this@ForgotPasswordFragment, Observer {
                        it?.let {
                            if (it == 1) {
                                confirm = 1
                                tvSentAgain.visibility = View.GONE
                                tvTitle.setText(R.string.nhap_ma_xac_thuc)
                                btnConfirm.setText(R.string.xac_thuc)
                                llInput.visibility = View.GONE
                                lnNumber.visibility = View.VISIBLE
                                counter = 61
                                mTimeLeftInMillis = START_TIME_IN_MILLIS
                                countDownTime()
                            }
                        }
                    })

                    data.observe(this@ForgotPasswordFragment, Observer {
                        it.let {
                            uid = it
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

    fun countDownTime() {
        yourCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                counter--
                tvCountSeconds.text = counter.toString().plus("s")
            }

            override fun onFinish() {
                tvCountSeconds.text = getString(R.string.time_second)
                tvSentAgain.visibility = View.VISIBLE
            }
        }.start()
    }

    override fun onStop() {
        yourCountDownTimer.cancel()
        super.onStop()
    }

    fun initViewModelValidateVerifyCode() {
        activity?.let {
            validateVerifyCodeViewModel =
                ViewModelProviders.of(this).get(ValidateVerifyCodeViewModel::class.java).apply {
                    verifyCodeSuccess.observe(this@ForgotPasswordFragment, Observer {
                        it?.let {
                            if (it) {
                                llEnterAuthCode.visibility = View.GONE
                                llForgotPass.visibility = View.VISIBLE
                            }
                        }
                    })

                    message.observe(this@ForgotPasswordFragment, Observer {
                        it?.let {
                            showToast(it)
                        }
                    })
                    verifyCodeFailr.observe(this@ForgotPasswordFragment, Observer {

                    })
                    loadingStatus.observe(this@ForgotPasswordFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }

    fun initchangePasswordViewModel() {
        activity.let {
            changePasswordViewModel =
                ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java).apply {
                    changePassword.observe(this@ForgotPasswordFragment, Observer {
                        if (it) {
                            if (it) {
                                llChangePassSuccessfully.visibility = View.VISIBLE
                                llForgotPass.visibility = View.GONE
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


}
