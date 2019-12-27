package com.example.alohaandroid.ui.aa_splash

import android.content.Intent
import android.os.Bundle
import com.example.alohaandroid.R
import com.example.alohaandroid.ui.aa_welcome.WelcomeActivity
import com.example.alohaandroid.ui.a_base.BaseActivity
import com.example.alohaandroid.utils.linphone.LinphoneManager
import com.example.alohaandroid.utils.linphone.LinphoneService
import com.example.alohaandroid.utils.linphone.LinphoneUtils


class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

//        checkNetwork()

    }

    override fun onStart() {
        super.onStart()
        if (LinphoneService.isReady()) {
            onServiceReady()
        } else {
            startService(
                Intent().setClass(this, LinphoneService::class.java)
            )
            ServiceWaitThread().start()
    }
    }

    private fun onServiceReady() {
        LinphoneUtils.dispatchOnUIThreadAfter(
            {
                val intent = Intent()
                intent.setClass(this, WelcomeActivity::class.java)
                if (getIntent() != null && getIntent().extras != null) {
                    intent.putExtras(getIntent().extras!!)
                }
                intent.action = getIntent().action
                intent.type = getIntent().type
                intent.data = getIntent().data
                startActivity(intent)
                finish()
            },
            100
        )
        LinphoneManager.getInstance().changeStatusToOnline()
    }

    private inner class ServiceWaitThread : Thread() {
        override fun run() {
            while (!LinphoneService.isReady()) {
                try {
                    Thread.sleep(30)
                } catch (e: InterruptedException) {
                    throw RuntimeException("waiting thread sleep() has been interrupted")
                }

            }
            LinphoneUtils.dispatchOnUIThread(
                Runnable { onServiceReady() })
        }
    }

//    private fun checkNetwork() {
//        if (!NetworkUtils.isNetworkAvailable(this)) {
//            showDialogNoNetwork()
//        }
//    }
//
//    private fun showDialogNoNetwork() {
//        AlertDialog.Builder(this, R.style.AlertDialogTheme)
//            .setTitle(getString(R.string.error_message_lost_internet_connection))
//            .setPositiveButton(R.string.button_try_again) { _, _ -> checkNetwork() }
//            .setNegativeButton(R.string.button_cancel) { dialog, _ -> dialog.dismiss() }
//            .show()
//    }


}
