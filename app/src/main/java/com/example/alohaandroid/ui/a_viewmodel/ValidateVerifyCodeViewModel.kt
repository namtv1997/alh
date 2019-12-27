package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class ValidateVerifyCodeViewModel:BaseViewModel() {

    val verifyCodeSuccess = MutableLiveData<Boolean>()
    val verifyCodeFailr = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun verifyCode(code:String){

        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uidRegister =  SharePrefs().getInstance()[Common.UID_REGISTER, String::class.java]

        addDisposable(
            API.service.validateVerifyCode("Bearer $token","$uidRegister",code)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({

                verifyCodeSuccess.postValue(it.data)
                message.postValue(it.message)

            },{

                verifyCodeFailr.postValue(it.message)

            })
        )
    }
}