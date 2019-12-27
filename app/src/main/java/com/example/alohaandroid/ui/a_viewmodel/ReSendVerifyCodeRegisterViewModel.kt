package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class ReSendVerifyCodeRegisterViewModel : BaseViewModel() {
    val reSendVerifyCodeSuccess = MutableLiveData<Boolean>()
    val reSendVerifyCodeFailr = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun reSendverifyCode(type: Int) {

        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uidRegister = SharePrefs().getInstance()[Common.UID_REGISTER, String::class.java]

        addDisposable(
            API.service.reSendVerifyCode("Bearer $token", "$uidRegister", type)
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe({

                    reSendVerifyCodeSuccess.postValue(it.data)
                    message.postValue(it.message)

                }, {

                    reSendVerifyCodeFailr.postValue(it.message)

                })
        )
    }
}