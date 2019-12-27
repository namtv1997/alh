package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.domain.remote.pojo.response.Hotline
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class GetAllHotlineViewModel : BaseViewModel() {
    val list = MutableLiveData<List<Hotline>>()
    val message = MutableLiveData<String>()

    fun getAllHotline() {
        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.getAllHotline("Bearer $token")
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe({

                    list.postValue(it.data)
                    message.postValue(it.message)

                }, {


                })
        )
    }
}