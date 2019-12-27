package com.example.alohaandroid.ui.ae_phone.addcontact

import androidx.lifecycle.MutableLiveData
import com.example.alohaandroid.domain.remote.api.API
import com.example.alohaandroid.ui.a_base.BaseViewModel
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.extension.SharePrefs
import com.example.alohaandroid.utils.extension.observeOnMain
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class UploadAvatarViewModel :BaseViewModel() {

    val uploadAvatarSuccess = MutableLiveData<Boolean>()
    val  uploadAvatarFailr = MutableLiveData<String>()
    val message = MutableLiveData<String>()

    fun uploadUserAvatar(avaFilePath: String) {
        val token =  SharePrefs().getInstance()[Common.ACCESS_TOKEN, String::class.java]
        val id =  SharePrefs().getInstance()[Common.ID_CREATE_CONTACT, Int::class.java]

        val rbID = RequestBody.create(MediaType.parse("text/plain"), id.toString())
        val body: MultipartBody.Part?
        if (avaFilePath.isEmpty()) {
            body = null
        } else {
            val file = File(avaFilePath)
            val fbody = RequestBody.create(MediaType.parse("image/*"), file)
            body = MultipartBody.Part.createFormData("fileName", file.name, fbody)
        }
        addDisposable(
            API.service.uploadAvatarProfile("Bearer $token",rbID,body!!)
            .observeOnMain()
            .doOnSubscribe { loadingStatus.postValue(true) }
            .doOnNext { loadingStatus.postValue(false) }
            .doOnError { loadingStatus.postValue(false) }
            .subscribe({
                uploadAvatarSuccess.postValue(true)
            },{
                uploadAvatarFailr.postValue(it.message)
            }))
    }


}