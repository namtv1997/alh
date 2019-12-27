package com.example.alohaandroid.domain.remote.pojo.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class Message (
    @SerializedName("messageID")
    @Expose
    private val messageID: Int? = null,
    @SerializedName("conversationID")
    @Expose
    private val conversationID: Int? = null,
    @SerializedName("fromJID")
    @Expose
    private val fromJID: String? = null,
    @SerializedName("fromJIDResource")
    @Expose
    private val fromJIDResource: String? = null,
    @SerializedName("toJID")
    @Expose
    private val toJID: String? = null,
    @SerializedName("toJIDResource")
    @Expose
    private val toJIDResource: String? = null,
    @SerializedName("sentDate")
    @Expose
    private val sentDate: Int? = null,
    @SerializedName("stanza")
    @Expose
    private val stanza: String? = null,
    @SerializedName("body")
    @Expose
    private val body: String? = null,
    @SerializedName("rowguid")
    @Expose
    private val rowguid: String? = null
)