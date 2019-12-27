package com.example.alohaandroid.domain.remote.pojo.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("Id")
    @Expose
    var id: Int? = null,
    @SerializedName("Phone")
    @Expose
    var phone: String? = null,
    @SerializedName("FullName")
    @Expose
    var fullName: String? = null,
    @SerializedName("Type")
    @Expose
    val type: Int? = null,
    @SerializedName("Email")
    @Expose
    var email: String? = null,
    @SerializedName("Avatar")
    @Expose
    var avatar: String? = null,
    @SerializedName("UID")
    @Expose
    val uID: String? = null
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(phone)
        parcel.writeString(fullName)
        parcel.writeValue(type)
        parcel.writeString(email)
        parcel.writeString(avatar)
        parcel.writeString(uID)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}