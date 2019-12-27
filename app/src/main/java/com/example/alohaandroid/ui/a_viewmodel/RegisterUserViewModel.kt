package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class RegisterUserViewModel : BaseViewModel() {

    val registerSuccess = MutableLiveData<Int>()
    val message = MutableLiveData<String>()
    fun register(
        emailID: String,
        password: String,
        fullName: String,
        phone: String,
        avatar: String,
        iD_Facebook: String,
        iD_Google: String,
        iD_Zalo: String
    ) {

        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]

        addDisposable(
            API.service.registerUser(
                "Bearer $token",
                emailID,
                "",
                password,
                fullName,
                phone,
                SharePrefs().getInstance()[Common.FIREBASE_TOKEN, String::class.java].toString(),
                avatar,
                iD_Facebook,
                iD_Google,
                iD_Zalo
            )
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe({

                    registerSuccess.postValue(it.status)
                    message.postValue(it.message)
                    it.data?.let { it1 -> handleregisterResponse(it1) }

                }, {
                })
        )
    }

    private fun handleregisterResponse(uidRegister: String) {

        SharePrefs().getInstance().put(Common.UID_REGISTER, uidRegister)

    }
}