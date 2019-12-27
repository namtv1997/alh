package com.example.alohaandroid.ui.a_base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.aloha_call_android.ui.widget.NoNetworkDialog
import com.example.aloha_call_android.utils.NetworkUtils
import com.example.alohaandroid.R
import com.example.alohaandroid.data.LocaleManager
import com.example.alohaandroid.ui.widget.CircleProgressDialog
import kotlinx.android.synthetic.main.include_toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    private lateinit var progressDialog: CircleProgressDialog
    private lateinit var noNetworkDialog: NoNetworkDialog

    fun setTitleActionBar(toolbar: Toolbar, title: String) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            tvTitleToolbar.text = title
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)
        }
    }

    fun setTitleActionBarRegister(toolbar: Toolbar, title: String) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            tvTitleToolbar.text = title
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_back)

            it.setBackgroundDrawable(ColorDrawable(Color.parseColor("#1D63F0")))
        }
    }

    fun setTitleActionBarCancel(toolbar: Toolbar, title: String) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        actionBar?.let {
            tvTitleToolbar.text = title
            it.title = ""
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_cancel)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase!!))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = CircleProgressDialog(this)
        noNetworkDialog = NoNetworkDialog(this)

        noNetworkDialog.setOnCancelListener {

            this.finish()
        }

        showViewNoNetWork()
    }

    fun showOrHideProgressDialog(isShow: Boolean) {
        when (isShow) {
            true -> progressDialog.show()
            else -> progressDialog.hide()
        }
    }

    fun showViewNoNetWork() {
        if (NetworkUtils.isNetworkAvailable(this)) {
            noNetworkDialog.dismiss()
        } else noNetworkDialog.show()
    }

    /**
     * Close SoftKeyboard when user click out of EditText
     */
//    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            val v = currentFocus
//            if (v is EditText) {
//                val outRect = Rect()
//                v.getGlobalVisibleRect(outRect)
//                if (!outRect.contains(event.rawX.toInt(), event.rawY.toInt())) {
//                    v.clearFocus()
//                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(v.windowToken, 0)
//                }
//            }
//        }
//        return super.dispatchTouchEvent(event)
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog.cancel()
    }
}