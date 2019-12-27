package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Hotline(
    @SerializedName("Id")
    @Expose
    var id: Int? = null,
    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = null,
    @SerializedName("Description")
    @Expose
    var description: String? = null,
    @SerializedName("Status")
    @Expose
    var status: Int? = null,
    @SerializedName("CreateDate")
    @Expose
    var createDate: String? = null
)
