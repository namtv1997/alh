package com.example.alohaandroid.utils.extension

import android.content.Context
import android.content.SharedPreferences
import com.example.alohaandroid.domain.remote.pojo.response.Token
import com.example.alohaandroid.utils.Common
import com.example.alohaandroid.utils.MyApplication
import com.google.gson.Gson


class SharePrefs {
    private var mInstance: SharePrefs? = null


    companion object {
        lateinit var mSharedPreferences: SharedPreferences
    }

    init {
        mSharedPreferences = MyApplication.self().getSharedPreferences(Common.PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun getInstance(): SharePrefs {
        if (mInstance == null) {
            mInstance = this
        }
        return mInstance as SharePrefs
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> get(key: String, anonymousClass: Class<T>): T? {
        return when (anonymousClass) {
            String::class.java -> mSharedPreferences.getString(key, "") as T
            Boolean::class.java -> java.lang.Boolean.valueOf(mSharedPreferences.getBoolean(key, false)) as T
            Float::class.java -> java.lang.Float.valueOf(mSharedPreferences.getFloat(key, 0f)) as T
            Int::class.java -> Integer.valueOf(mSharedPreferences.getInt(key, 0)) as T
            Long::class.java -> java.lang.Long.valueOf(mSharedPreferences.getLong(key, 0)) as T
            else -> null
        }
    }

    fun <T> put(key: String, data: T) {
        val editor = mSharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data as String)
            is Boolean -> editor.putBoolean(key, data as Boolean)
            is Float -> editor.putFloat(key, data as Float)
            is Int -> editor.putInt(key, data as Int)
            is Long -> editor.putLong(key, data as Long)
        }
        editor.apply()
    }

//    fun saveDataCanBo(canBo: Token) {
//        val prefsEditor = mSharedPreferences.edit()
//        val gson = Gson()
//        val json = gson.toJson(canBo)
//        prefsEditor.putString(Common.DATA_USER, json)
//        prefsEditor.apply()
//    }
//
//    fun getDataCanBo(context: Context): Token {
//        val gson = Gson()
//        val json = mSharedPreferences.getString(Common.DATA_USER, "")
//        return gson.fromJson<Token>(json, Token::class.java!!)
//    }

    fun clear() {
        mSharedPreferences.edit().clear().apply()
    }
}