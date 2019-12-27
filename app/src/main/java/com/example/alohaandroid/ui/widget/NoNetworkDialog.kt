package com.example.aloha_call_android.ui.widget

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import com.example.aloha_call_android.utils.NetworkUtils
import com.example.alohaandroid.R
import kotlinx.android.synthetic.main.include_no_network.*



class NoNetworkDialog(context: Context) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.include_no_network)

        setCancelable(true)

        window?.setGravity(Gravity.CENTER)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        //window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)

        btnTryAgain.setOnClickListener {
//            if (NetworkUtils.isNetworkAvailable(context)) {
//
//            }

            dismiss()
        }
    }
}