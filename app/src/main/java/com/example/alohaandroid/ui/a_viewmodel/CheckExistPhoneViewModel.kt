package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CheckExistPhoneViewModel:BaseViewModel() {
    val phoneExist = MutableLiveData<Boolean>()
    val phoneError = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun checkExistPhone(phone:String){
        val token=  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.CheckExistPhone("Bearer $token",phone)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe(
                {
                    phoneExist.postValue(it.data)
                    message.postValue(it.message)
                },
                {
                    phoneError.postValue(it.message)
                }

            ))

    }
}