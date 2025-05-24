package com.pwr.BookPlatform.data.session

import android.content.Context
import android.content.SharedPreferences
import com.pwr.BookPlatform.data.models.AuthResponse
import com.pwr.BookPlatform.data.models.UserResponse

object UserSession {
    private lateinit var appContext: Context
    private const val PREF_NAME = "auth"
    private const val KEY_AUTH_TOKEN = "token"
    var user: UserResponse? = null
    var token: String? = null

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    fun saveAuthToken(token: String, user: UserResponse) {
        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(KEY_AUTH_TOKEN, token).apply()

        this.token = token
        this.user = user
    }

    fun getAuthToken() {
        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        token = sharedPreferences.getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        val sharedPreferences: SharedPreferences = appContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().remove(KEY_AUTH_TOKEN).apply()
    }
}