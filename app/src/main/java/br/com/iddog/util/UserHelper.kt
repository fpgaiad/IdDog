package br.com.iddog.util

import android.content.SharedPreferences

object UserHelper {

    const val INVALID_EMAIL = "invalidEmail"
    const val EMAIL_KEY = "email"
    const val TOKEN_KEY = "token"

    private var mStorage: SharedPreferences? = null

    fun setStorage(storage: SharedPreferences?) {
        mStorage = storage
    }

    fun saveUser(email: String, token: String) {
        val editor = mStorage?.edit()
        editor?.apply {
            putString(EMAIL_KEY, email)
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    fun getStorage(): SharedPreferences? {
        return mStorage
    }
}