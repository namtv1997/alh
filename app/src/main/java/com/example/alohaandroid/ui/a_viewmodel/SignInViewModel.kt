package com.example.alohaandroid.ui.a_viewmodel


import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API

import com.example.alohaandroid.domain.remote.pojo.response.Token
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain


class SignInViewModel : BaseViewModel() {
    val loginSuccess = MutableLiveData<Token>()
    val loginState = MutableLiveData<String>()
    val error = MutableLiveData<String>()

    fun loginWithApp(password: String, username: String, type_account: Int) {

        addDisposable(API.service.login(
            "password",
            password,
            SharePrefs().getInstance()[Common.FIREBASE_TOKEN, String::class.java].toString(),
            username,
            type_account
        )
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe(
                {
                    loginSuccess.postValue(it)
                    handleLoginResponse(it)
                },
                {
                    loginState.postValue(it.message)
                }

            ))
    }

    private fun handleLoginResponse(token: Token) {
        //Save user token to pref
        SharePrefs().getInstance().put(Common.ACCESS_TOKEN, token.accessToken)
        if (token.uid == "999999999" || token.uid == "9001") {

        } else {
            SharePrefs().getInstance().put(Common.UID, token.uid)

        }
    }


}