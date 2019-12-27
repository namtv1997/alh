package com.example.alohaandroid.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferUtilJava {
    public static void saveString(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("mFilePath", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key, String defaultValue) {
        SharedPreferences preferences = context.getSharedPreferences("mFilePath", Context.MODE_PRIVATE);
        return preferences.getString(key, defaultValue);
    }
}
