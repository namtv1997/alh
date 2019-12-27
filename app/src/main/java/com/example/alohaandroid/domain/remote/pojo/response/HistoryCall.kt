package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class HistoryCall(
    @SerializedName("Id")
    @Expose
     val id: Int? = null,
    @SerializedName("DateCreate")
    @Expose
    var dateCreate: String? = null,
    @SerializedName("Phone")
    @Expose
    var phone: String? = null,
    @SerializedName("TimeCall")
    @Expose
    var timeCall: Int? = null,
    @SerializedName("Type")
    @Expose
     val type: Int? = null,
    @SerializedName("Status")
    @Expose
     val status: Int? = null


)