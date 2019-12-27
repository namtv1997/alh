package com.example.alohaandroid.domain.remote.api

import com.example.alohaandroid.domain.remote.pojo.response.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("token")
    @FormUrlEncoded
    fun login(
        @Field("grant_type") grant_type: String,
        @Field("password") password: String,
        @Field("firebase_token") firebase_token: String,
        @Field("username") username: String,
        @Field("type_account") type_account: Int
    ): Observable<Token>

    @GET("api/User/CheckExistEmail")
    fun CheckExistEmail(
        @Header("Authorization") token: String,
        @Query("email") email: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/User/CheckExistUsername")
    fun CheckExistUsername(
        @Header("Authorization") token: String,
        @Query("username") username: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/User/CheckExistPhone")
    fun CheckExistPhone(
        @Header("Authorization") token: String,
        @Query("phone") phone: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/Contact/getAll")
    fun getAllContact(
        @Header("Authorization") token: String,
        @Query("uid") uid: String
    ): Observable<BaseResponse<List<Contact>>>

    @GET("api/HistoryCall/getAll")
    fun getAllHistoryCall(
        @Header("Authorization") token: String,
        @Query("uid") uid: String,
        @Query("code") code: String
    ): Observable<BaseResponse<List<HistoryCall>>>

    @GET("api/User/getUser")
    fun getInforUser(
        @Header("Authorization") token: String,
        @Query("uid") uid: String
    ): Observable<BaseResponse<InforUser>>

    @GET("api/Contact/getById")
    fun getByIdContact(
        @Header("Authorization") token: String,
        @Query("Id") Id: Int
    ): Observable<BaseResponse<InforContact>>

    @POST("api/User/Register")
    @FormUrlEncoded
    fun registerUser(
        @Header("Authorization") token: String,
        @Field("EmailID") EmailID: String,
        @Field("UserName") UserName: String,
        @Field("Password") Password: String,
        @Field("FullName") FullName: String,
        @Field("Phone") Phone: String,
        @Field("FirebaseToken") FirebaseToken: String,
        @Field("Avatar") Avatar: String,
        @Field("ID_Facebook") ID_Facebook: String,
        @Field("ID_Google") ID_Google: String,
        @Field("ID_Zalo") ID_Zalo: String
    ): Observable<BaseResponse<String>>

    @POST("api/User/ForgotPassword")
    @FormUrlEncoded
    fun forgotPassword(
        @Header("Authorization") token: String,
        @Field("EmailID") EmailID: String,
        @Field("Phone") Phone: String
    ): Observable<BaseResponse<String>>

    @POST("api/Contact/Create")
    @FormUrlEncoded
    fun createContact(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("Phone") Phone: String,
        @Field("FullName") FullName: String,
        @Field("Email") Email: String,
        @Field("Type") Type: Int,
        @Field("IdUser") IdUser: Int
    ): Observable<BaseResponse<Int>>

    @POST("api/User/ValidateVerifyCode")
    @FormUrlEncoded
    fun validateVerifyCode(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("code") code: String
    ): Observable<BaseResponse<Boolean>>

    @POST("api/User/ChangePassword")
    @FormUrlEncoded
    fun changePassword(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("Password") Password: String
    ): Observable<BaseResponse<Boolean>>

    @DELETE("api/Contact/Delete")
    fun deleteContact(
        @Header("Authorization") token: String,
        @Query("Id") Id: Int
    ): Observable<BaseResponse<Boolean>>

    @POST("api/User/ReSendVerifyCodeRegister")
    @FormUrlEncoded
    fun reSendVerifyCode(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("Type") Type: Int
    ): Observable<BaseResponse<Boolean>>

    @Multipart
    @POST("api/Contact/UploadAvatar")
    fun uploadAvatarProfile(
        @Header("Authorization") token: String,
        @Part("Id") Id: RequestBody,
        @Part image: MultipartBody.Part
    ): Observable<BaseResponse<Boolean>>

    @GET("api/User/CheckExistIDFacebook")
    fun CheckExistIDFacebook(
        @Header("Authorization") token: String,
        @Query("id_facebook") id_facebook: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/User/CheckExistIDGoogle")
    fun CheckExistIDGoogle(
        @Header("Authorization") token: String,
        @Query("id_google") id_google: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/User/CheckExistIDZalo")
    fun CheckExistIDZalo(
        @Header("Authorization") token: String,
        @Query("id_zalo") id_zalo: String
    ): Observable<BaseResponse<Boolean>>

    @POST("api/User/LogOut")
    @FormUrlEncoded
    fun LogOut(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("firebase_token") firebase_token: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/project/getAll")
    fun getAllProject(
        @Header("Authorization") token: String,
        @Query("uid") uid: String
    ): Observable<BaseResponse<List<Project>>>

    @GET("api/project/CheckExistName")
    fun CheckExistName(
        @Header("Authorization") token: String,
        @Query("uid") uid: String,
        @Query("name") name: String
    ): Observable<BaseResponse<Boolean>>

    @POST("api/project/Create")
    @FormUrlEncoded
    fun Create(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("Name") Name: String,
        @Field("Description") Description: String
    ): Observable<BaseResponse<Int>>

    @POST("api/project/Update")
    @FormUrlEncoded
    fun Update(
        @Header("Authorization") token: String,
        @Field("Id") Id: Int,
        @Field("Name") Name: String,
        @Field("Description") Description: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/Hotline/getAll")
    fun getAllHotline(
        @Header("Authorization") token: String
    ): Observable<BaseResponse<List<Hotline>>>

    @POST("api/Hotline/AddHotline")
    @FormUrlEncoded
    fun AddHotline(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("idProject") idProject: Int,
        @Field("IdHotLines") IdHotLines: String
    ): Observable<BaseResponse<Boolean>>

    @POST("api/Hotline/DeleteHotline")
    @FormUrlEncoded
    fun DeleteHotline(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("idProject") idProject: Int,
        @Field("IdHotLine") IdHotLines: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/Hotline/getAllByProject")
    fun getAllByProject(
        @Header("Authorization") token: String,
        @Query("code") code: String
    ): Observable<BaseResponse<List<Hotline>>>

    @DELETE("api/project/Delete")
    fun DeleteProject(
        @Header("Authorization") token: String,
        @Query("Id") Id: Int
    ): Observable<BaseResponse<Boolean>>

    @POST("api/HistoryCall/Create")
    @FormUrlEncoded
    fun CreateHistoryCall(
        @Header("Authorization") token: String,
        @Field("UID") UID: String,
        @Field("DateCreate") DateCreate: String,
        @Field("Phone") Phone: String,
        @Field("TimeCall") TimeCall: Int,
        @Field("Type") Type: Int,
        @Field("Status") Status: Int,
        @Field("ProjectCode") ProjectCode: String
    ): Observable<BaseResponse<Boolean>>

    @GET("api/HistoryCall/search")
    fun searchHistoryCall(
        @Header("Authorization") token: String,
        @Query("uid") uid: String,
        @Query("code") code: String,
        @Query("start") start: String,
        @Query("end") end: String,
        @Query("type") type: Int,
        @Query("status") status: Int,
        @Query("phone") phone: String
    ): Observable<BaseResponse<List<HistoryCall>>>

    @GET("api/HistoryCall/getById")
    fun getByIdHistoryCall(
        @Header("Authorization") token: String,
        @Query("Id") Id: Int
    ): Observable<BaseResponse<List<HistoryCall>>>

    @DELETE("api/HistoryCall/Delete")
    fun DeleteHistoryCall(
        @Header("Authorization") token: String,
        @Query("Id") Id: Int
    ): Observable<BaseResponse<Boolean>>

    @GET("api/HistoryMessage/getAll")
    fun getAllHistoryMessage(
        @Header("Authorization") token: String,
        @Query("uid") uid: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Observable<BaseResponse<List<Message>>>
}