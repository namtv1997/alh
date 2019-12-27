package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.domain.remote.pojo.response.InforUser
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class GetUserViewModel :BaseViewModel() {
    val userInfor= MutableLiveData<InforUser>()

    fun getuserInfor(){
        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uid =  SharePrefs().getInstance()[Common.UID, String::class.java]
        addDisposable(
            API.service.getInforUser("Bearer $token","$uid")
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({

                userInfor.postValue(it.data)

            },{


            })
        )
    }
}