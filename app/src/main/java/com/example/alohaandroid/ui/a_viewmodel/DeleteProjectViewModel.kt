package com.example.alohaandroid.ui.a_viewmodel

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class DeleteProjectViewModel :BaseViewModel() {
    val deleteSuccess = MutableLiveData<Boolean>()
    val deleteFailr = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun deleteProject(Id: Int){

        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]

        addDisposable(
            API.service.DeleteProject("Bearer $token",Id)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({
                deleteSuccess.postValue(it.data)
                message.postValue(it.message)

            },{
                deleteFailr.postValue(it.message)
            })
        )
    }
}