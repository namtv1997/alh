package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CheckExistIDFacebookViewModel : BaseViewModel() {

    val Exist = MutableLiveData<Boolean>()
    val error = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun CheckExistIDFacebookViewModel(id_facebook: String) {
        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.CheckExistIDFacebook("Bearer $token", id_facebook)
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