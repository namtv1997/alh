package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CheckExistIDZaloViewModel : BaseViewModel() {

    val Exist = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun CheckExistIDZalo(id_zalo: String) {
        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.CheckExistIDZalo("Bearer $token", id_zalo)
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe(
                    {
                        Exist.postValue(it.data)
                        message.postValue(it.message)
                    },
                    {
                        error.postValue(it.message)
                    }

                ))

    }
}