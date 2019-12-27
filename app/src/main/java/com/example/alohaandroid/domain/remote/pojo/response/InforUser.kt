package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName



data class InforUser(
    @SerializedName("Id")
    @Expose
     val id: Int? = null,
    @SerializedName("UID")
    @Expose
     val uID: String? = null,
    @SerializedName("EmailID")
    @Expose
     val emailID: String? = null,
    @SerializedName("UserName")
    @Expose
     val userName: String? = null,
    @SerializedName("FullName")
    @Expose
     val fullName: String? = null,
    @SerializedName("Phone")
    @Expose
     val phone: String? = null,
    @SerializedName("TotalMoney")
    @Expose
     val totalMoney: Double? = null,
    @SerializedName("Avatar")
    @Expose
     val avatar: String? = null,
    @SerializedName("Notifications")
    @Expose
     val notifications: Boolean? = null,
    @SerializedName("AccountSIP")
    @Expose
     val accountSIP: String? = null,
    @SerializedName("PasswrodSIP")
    @Expose
     val passwrodSIP: String? = null
)