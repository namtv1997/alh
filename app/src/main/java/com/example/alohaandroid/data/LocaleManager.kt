package com.example.alohaandroid.data

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import com.example.alohaandroid.utils.extension.edit
import java.util.*

class LocaleManager {

    companion object {
        const val ENGLISH = "en"
        const val VIETNAMESE = "vi"

        const val LANGUAGE_KEY = "language_key"

        fun setLocale(context: Context): Context = updateResource(context, getLanguage(context))

        fun setNewLocale(context: Context, language: String) {
            persistLanguage(context, language)
        }

        fun getLanguage(context: Context): String {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)

            var defaultLang = VIETNAMESE
            val localLang = Locale.getDefault().language
            when (localLang) {
                ENGLISH -> defaultLang = ENGLISH
            }

            return prefs.getString(LANGUAGE_KEY, defaultLang) ?: VIETNAMESE
        }

        private fun persistLanguage(context: Context, language: String) {
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            prefs.edit { putString(LANGUAGE_KEY, language) }
        }

        private fun updateResource(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }

    }
}