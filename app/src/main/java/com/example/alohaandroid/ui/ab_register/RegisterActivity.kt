package com.example.alohaandroid.ui.ab_register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.ab_login.RegisterFragment
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.utils.extension.intentToFragmentArguments
import com.example.alohaandroid.utils.extension.replaceFragmentInActivity
import kotlinx.android.synthetic.main.include_toolbar.*

class RegisterActivity : BaseActivity() {
    private var idRegister: Int ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val intent = intent
        if (intent != null) {
            if (intent.hasExtra(EXTRA_ID)) {
                idRegister = intent.getIntExtra(EXTRA_ID, 0)
            }
        }
        setTitleActionBarRegister(toolbar,"")
        replaceFragmentInActivity(findOrCreateView(), R.id.frameContent)
    }

    private fun findOrCreateView() =
        supportFragmentManager.findFragmentById(R.id.frameContent) ?: RegisterFragment().apply {
            arguments = intentToFragmentArguments(intent)
        }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, RegisterActivity::class.java))
        }

        val EXTRA_ID = "EXTRA_ID"
    }

}
