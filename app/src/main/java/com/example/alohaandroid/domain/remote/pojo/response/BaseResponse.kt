package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

open class BaseResponse<T> {

    @SerializedName("status")
    @Expose
     var status: Int? = null
    @SerializedName("message")
    @Expose
     var message: String? = null
    @SerializedName("data")
    @Expose
     var data: T? = null
}