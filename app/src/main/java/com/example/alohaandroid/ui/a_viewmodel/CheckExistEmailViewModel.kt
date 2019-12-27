package com.example.alohaandroid.ui.a_viewmodel


import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CheckExistEmailViewModel : BaseViewModel() {
    val emailExist = MutableLiveData<Boolean>()
    val errorEmail = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun checkExistEmail(email: String) {
        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.CheckExistEmail("Bearer $token", email)
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe(
                    {
                        emailExist.postValue(it.data)
                        message.postValue(it.message)
                    },
                    {
                        errorEmail.postValue(it.message)
                    }

                ))

    }
}