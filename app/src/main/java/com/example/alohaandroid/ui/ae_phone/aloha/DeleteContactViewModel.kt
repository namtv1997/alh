package com.example.alohaandroid.ui.ae_phone.aloha

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class DeleteContactViewModel :BaseViewModel() {
    val deleteContactSuccess = MutableLiveData<Boolean>()
    val deleteContactFailr = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun deleteContact(Id: Int){

        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]

        addDisposable(
            API.service.deleteContact("Bearer $token",Id)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({
                deleteContactSuccess.postValue(it.data)
                message.postValue(it.message)

            },{
                deleteContactFailr.postValue(it.message)
            })
        )
    }
}