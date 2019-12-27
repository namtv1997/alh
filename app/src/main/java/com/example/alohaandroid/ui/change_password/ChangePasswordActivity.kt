package com.example.alohaandroid.ui.change_password

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.showToast
import kotlinx.android.synthetic.main.activity_change_password.*
import kotlinx.android.synthetic.main.toolbar_personal.*

class ChangePasswordActivity : BaseActivity(), View.OnClickListener {

    private lateinit var changePasswordViewModel: ChangePasswordViewModel
    val uid = SharePrefs().getInstance()[Common.UID, String::class.java]
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        ivBack.setOnClickListener(this)
        tvTitleRight.setOnClickListener(this)
        tvTitleLeft.text = "Đổi mật khẩu"
        tvTitleRight.text = "Lưu"

        initchangePasswordViewModel()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivBack -> {
                finish()
            }
            R.id.tvTitleRight -> {
                val reNewPassWord = edtInputReNewPassword.text.toString()
                val newPassWord = edtInputNewPassword.text.toString()
                if (reNewPassWord != newPassWord) {
                    Toast.makeText(this, R.string.error_incorrect_pasword, Toast.LENGTH_LONG).show()
                } else {
                    if (uid != null) {
                        changePasswordViewModel.changePassword(uid, reNewPassWord)
                    }
                }

            }
        }
    }

    fun initchangePasswordViewModel() {
        changePasswordViewModel =
            ViewModelProviders.of(this).get(ChangePasswordViewModel::class.java).apply {
                changePassword.observe(this@ChangePasswordActivity, Observer {
                    if (it) {
                        finish()
                    }

                })
                message.observe(this@ChangePasswordActivity, Observer {
                    it?.let {
                        showToast(it)
                    }
                })
                loadingStatus.observe(this@ChangePasswordActivity, Observer {
                    showOrHideProgressDialog(it)
                })
            }
    }
}
