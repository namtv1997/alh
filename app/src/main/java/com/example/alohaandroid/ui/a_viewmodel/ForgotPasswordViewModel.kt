package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class ForgotPasswordViewModel : BaseViewModel() {
    val password = MutableLiveData<Int>()
    val message = MutableLiveData<String>()
    val data = MutableLiveData<String>()

    fun forgotPassword(emailID: String, phone: String) {

        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]

        addDisposable(
            API.service.forgotPassword("Bearer ${token}", emailID, phone)
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe({
                    data.postValue(it.data)
                    password.postValue(it.status)
                    message.postValue(it.message)

                }, {

                })
        )
    }

}