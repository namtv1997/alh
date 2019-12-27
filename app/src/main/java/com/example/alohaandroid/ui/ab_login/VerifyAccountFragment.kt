package com.example.alohaandroid.ui.ab_login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_viewmodel.ReSendVerifyCodeRegisterViewModel
import com.example.alohaandroid.ui.a_viewmodel.ValidateVerifyCodeViewModel
import com.example.alohaandroid.ui.aa_welcome.WelcomeActivity
import com.example.alohaandroid.ui.a_base.BaseFragment
import com.example.alohaandroid.utils.extension.alertDialog
import com.example.alohaandroid.utils.extension.showToast
import kotlinx.android.synthetic.main.fragment_verify_account.*

class VerifyAccountFragment : BaseFragment(), View.OnClickListener {

    private var confirm = 0
    private lateinit var yourCountDownTimer: CountDownTimer
    private var counter: Int = 61
    private val START_TIME_IN_MILLIS: Long = 61000
    private var mTimeLeftInMillis = START_TIME_IN_MILLIS
    private var reSendVerifyCodeRegisterViewModel: ReSendVerifyCodeRegisterViewModel? = null
    private var validateVerifyCodeViewModel: ValidateVerifyCodeViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_verify_account, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewmodelReSendVerifyCodeRegister()
        initViewModelValidateVerifyCode()

        btnConfirm.setOnClickListener(this)
        ll.setOnClickListener(this)
        tvSentAgain.setOnClickListener(this)
        imBack.setOnClickListener(this)
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

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.imBack->{
                activity?.onBackPressed()
            }
            R.id.ll -> {
                closeKeyboard()
            }
            R.id.btnConfirm -> {
                if (confirm == 0) {
                    if (rbPhone.isChecked) {
                        reSendVerifyCodeRegisterViewModel?.reSendverifyCode(0)
                    } else if (rbEmail.isChecked) {
                        reSendVerifyCodeRegisterViewModel?.reSendverifyCode(1)
                    } else {
                        Toast.makeText(activity, R.string.chua_chon_noi_gui_ma, Toast.LENGTH_LONG)
                            .show()
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
                if (rbPhone.isChecked) {
                    reSendVerifyCodeRegisterViewModel?.reSendverifyCode(0)
                } else if (rbEmail.isChecked) {
                    reSendVerifyCodeRegisterViewModel?.reSendverifyCode(1)
                }

            }
        }
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

    private fun closeKeyboard() {
        val view = activity!!.currentFocus
        if (view != null) {
            val imm =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
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


    fun initViewmodelReSendVerifyCodeRegister() {
        activity?.let {
            reSendVerifyCodeRegisterViewModel =
                ViewModelProviders.of(this).get(ReSendVerifyCodeRegisterViewModel::class.java)
                    .apply {
                        reSendVerifyCodeSuccess.observe(this@VerifyAccountFragment, Observer {
                            it?.let {
                                alertDialog(getString(R.string.kiem_tra_ma_xac_thuc_dc_gui_ve))
                                confirm = 1
                                tvTitleVerify.setText(R.string.nhap_ma_xac_thuc)
                                btnConfirm.setText(R.string.xac_nhan)
                                tvSentAgain.visibility = View.GONE
                                rgVerificationCode.visibility = View.GONE
                                lnNumber.visibility = View.VISIBLE
                                counter = 61
                                mTimeLeftInMillis = START_TIME_IN_MILLIS
                                countDownTime()
                            }
                        })

                        message.observe(this@VerifyAccountFragment, Observer {
                            it?.let {
                                showToast(it)
                            }
                        })
                        reSendVerifyCodeFailr.observe(this@VerifyAccountFragment, Observer {

                        })
                        loadingStatus.observe(this@VerifyAccountFragment, Observer {
                            showOrHideProgressDialog(it)
                        })
                    }
        }
    }

    fun initViewModelValidateVerifyCode() {
        activity?.let {
            validateVerifyCodeViewModel =
                ViewModelProviders.of(this).get(ValidateVerifyCodeViewModel::class.java).apply {
                    verifyCodeSuccess.observe(this@VerifyAccountFragment, Observer {
                        it?.let {
                            llEnterAuthCode.visibility = View.GONE
                            llAuthenticationSuccessful.visibility = View.VISIBLE
                            Handler().postDelayed({
                                startActivity(Intent(activity, WelcomeActivity::class.java))
                                activity!!.finish()
                            }, 5000)
                        }
                    })

                    message.observe(this@VerifyAccountFragment, Observer {
                        it?.let {
                            showToast(it)
                        }
                    })
                    verifyCodeFailr.observe(this@VerifyAccountFragment, Observer {

                    })
                    loadingStatus.observe(this@VerifyAccountFragment, Observer {
                        showOrHideProgressDialog(it)
                    })
                }
        }
    }
}
