package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CheckExistUsernameViewModel :BaseViewModel(){
    val usernameExist = MutableLiveData<Boolean>()
    val usernameError = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun checkExistUserName(username:String){
        val token=  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.CheckExistUsername("Bearer $token",username)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe(
                {
                    usernameExist.postValue(it.data)
                    message.postValue(it.message)
                },
                {
                    usernameError.postValue(it.message)
                }

            ))

    }
}