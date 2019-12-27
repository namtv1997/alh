package com.example.alohaandroid.ui.ae_phone.aloha

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.domain.remote.pojo.response.Contact
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class GetallContactViewModel:BaseViewModel() {
    val listcontact=MutableLiveData<List<Contact>>()
    val message = MutableLiveData<String>()
    val getContactFailr = MutableLiveData<String>()

    fun getAllContact(){
        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uid =  SharePrefs().getInstance()[Common.UID, String::class.java]
        addDisposable(
            API.service.getAllContact("Bearer $token","$uid")
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({

                listcontact.postValue(it.data)
                message.postValue(it.message)

            },{

                getContactFailr.postValue(it.message)

            })
        )
    }
}