package com.example.alohaandroid.ui.change_password

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class ChangePasswordViewModel : BaseViewModel() {

    val changePassword = MutableLiveData<Boolean>()
    val changePassworderror = MutableLiveData<String>()
    val message = MutableLiveData<String>()


    fun changePassword(uid: String, password: String) {
        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        addDisposable(
            API.service.changePassword("Bearer $token", uid, password)
                .observeOnMain()
                .doOnSubscribe { loadingStatus.postValue(true) }
                .doOnNext { loadingStatus.postValue(false) }
                .doOnError { loadingStatus.postValue(false) }
                .subscribe(
                    {
                        changePassword.postValue(it.data)
                        message.postValue(it.message)
                    },
                    {
                        changePassworderror.postValue(it.message)
                    }

                ))

    }

}