package com.example.alohaandroid.ui.ae_phone.addcontact

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain

class CreateContactViewModel : BaseViewModel() {
    val createContactSuccess = MutableLiveData<Int>()
    val message = MutableLiveData<String>()

    fun createContact(phone: String, FullName: String, Email: String, Type: Int, IdUser: Int) {
        val token = SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val uid = SharePrefs().getInstance()[Common.UID, String::class.java]

        addDisposable(API.service.createContact(
            "Bearer ${token}",
            "$uid",
            phone,
            FullName,
            Email,
            Type,
            IdUser
        )
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({
                SharePrefs().getInstance().put(Common.ID_CREATE_CONTACT, it.data)
                createContactSuccess.postValue(it.data)
                message.postValue(it.message)

            }, {

            })
        )
    }
}