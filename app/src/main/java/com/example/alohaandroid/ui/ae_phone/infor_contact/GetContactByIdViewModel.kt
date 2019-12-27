package com.example.alohaandroid.ui.ae_phone.infor_contact

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.domain.remote.pojo.response.InforContact
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class GetContactByIdViewModel:BaseViewModel() {
    val contactInfor= MutableLiveData<InforContact>()

    fun getContactInfor(Id:Int){
        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.getByIdContact("Bearer $token",Id)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({

                contactInfor.postValue(it.data)

            },{


            })
        )
    }
}