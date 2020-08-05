package br.com.iddog.util

import android.content.SharedPreferences

object UserHelper {

    private var mEditor: SharedPreferences.Editor? = null
    private var mStorage: SharedPreferences? = null

    fun setStorage(storage: SharedPreferences?) {
        mStorage = storage
    }

    fun saveUser(email: String, token: String) {
        mEditor = mStorage?.edit()
        mEditor?.apply {
            putString("email", email)
            putString("token", token)
            apply()
        }
    }

    fun getStorage(): SharedPreferences? {
        return mStorage
    }
}