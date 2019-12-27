package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CreateViewModel : BaseViewModel() {
    val Success = MutableLiveData<Int>()
    val Failr = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun Create(name: String, description: String) {

        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uid = SharePrefs().getInstance()[Common.UID, String::class.java]

        addDisposable(
            API.service.Create("Bearer $token", uid.toString(), name, description)
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe({

                    Success.postValue(it.data)
                    message.postValue(it.message)

                }, {

                    Failr.postValue(it.message)

                })
        )
    }
}