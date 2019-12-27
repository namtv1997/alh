
package com.example.alohaandroid.ui.a_base
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.aloha_call_android.ui.widget.NoNetworkDialog
import com.example.aloha_call_android.utils.NetworkUtils
import com.example.alohaandroid.ui.widget.CircleProgressDialog

abstract class BaseFragment : Fragment() {

    private lateinit var progressDialog: CircleProgressDialog
    private lateinit var noNetworkDialog: NoNetworkDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = CircleProgressDialog(activity!!)
        noNetworkDialog = NoNetworkDialog(activity!!)

        noNetworkDialog.setOnCancelListener {
            activity!!.finish()
        }

        showViewNoNetWork()
    }

    fun showOrHideProgressDialog(isShow: Boolean) {
        when (isShow) {
            true -> progressDialog.show()
            else -> progressDialog.hide()
        }
    }

    override fun onPause() {
        super.onPause()
        progressDialog.cancel()
    }

    fun showViewNoNetWork() {
        if (NetworkUtils.isNetworkAvailable(activity!!)) {
            noNetworkDialog.dismiss()
        } else noNetworkDialog.show()
    }


}