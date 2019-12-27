package com.example.alohaandroid.utils

import android.annotation.SuppressLint
import android.app.Application
import com.example.alohaandroid.utils.extension.SharePrefs
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import com.zing.zalo.zalosdk.oauth.ZaloSDKApplication

@SuppressLint("Registered")
class MyApplication : Application() {
    var firebase: String? = null
    companion object {
        private lateinit var mSelf: MyApplication

        fun self(): MyApplication {
            return mSelf
        }
    }


    override fun onCreate() {
        super.onCreate()
        mSelf = this
        ZaloSDKApplication.wrap(this)
//        FirebaseApp.initializeApp(applicationContext)

//        firebase = FirebaseInstanceId.getInstance().getToken()
        firebase = "abc123456"
        SharePrefs().getInstance().put(Common.FIREBASE_TOKEN, firebase)

    }
}