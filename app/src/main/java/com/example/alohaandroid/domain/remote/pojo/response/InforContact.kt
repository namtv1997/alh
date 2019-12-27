package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class InforContact(
    @SerializedName("Id")
    @Expose
    val id: Int? = null,
    @SerializedName("Phone")
    @Expose
    val phone: String? = null,
    @SerializedName("FullName")
    @Expose
    val fullName: String? = null,
    @SerializedName("Type")
    @Expose
    val type: Int? = null,
    @SerializedName("Email")
    @Expose
    val email: String? = null,
    @SerializedName("Avatar")
    @Expose
    val avatar: String? = null,
    @SerializedName("UID")
    @Expose
    val uID: String? = null
)