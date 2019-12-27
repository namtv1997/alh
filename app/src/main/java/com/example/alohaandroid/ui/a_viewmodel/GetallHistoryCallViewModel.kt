package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.domain.remote.pojo.response.HistoryCall
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class GetallHistoryCallViewModel:BaseViewModel() {
    val listHistoryCall= MutableLiveData<List<HistoryCall>>()
    val message = MutableLiveData<String>()

    fun getAllHistoryCall(){
        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uid =  SharePrefs().getInstance()[Common.UID, String::class.java]
        val code = SharePrefs().getInstance()[Common.CODE, String::class.java]
        addDisposable(
            API.service.getAllHistoryCall("Bearer $token","$uid",code.toString())
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({

                listHistoryCall.postValue(it.data)
                message.postValue(it.message)

            },{


            })
        )
    }
}