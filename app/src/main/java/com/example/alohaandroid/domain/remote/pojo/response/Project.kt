package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Project(
    @SerializedName("Id")
    @Expose
    val id: Int? = null,
    @SerializedName("IdUser")
    @Expose
    val idUser: Int? = null,
    @SerializedName("Name")
    @Expose
    val name: String? = null,
    @SerializedName("Description")
    @Expose
    val description: String? = null,
    @SerializedName("Status")
    @Expose
    val status: Int? = null,
    @SerializedName("Code")
    @Expose
    val code: String? = null
)