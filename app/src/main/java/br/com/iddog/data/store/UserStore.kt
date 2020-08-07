package br.com.iddog.data.store

import android.content.SharedPreferences
import br.com.iddog.data.model.login.User
import javax.inject.Inject

class UserStore @Inject constructor(private val storage: SharedPreferences) : Store {

    companion object {
        const val INVALID_EMAIL = "invalidEmail"
        const val EMAIL_KEY = "email"
        const val TOKEN_KEY = "token"
    }

    override fun saveUser(email: String, token: String) {
        val editor = storage.edit()
        editor?.apply {
            putString(EMAIL_KEY, email)
            putString(TOKEN_KEY, token)
            apply()
        }
    }

    override fun getUser(): User? {
        return if (storage.contains(EMAIL_KEY)) {
                User(
                    storage.getString(EMAIL_KEY, INVALID_EMAIL) ?: "",
                    storage.getString(TOKEN_KEY, "") ?: ""
                )
        } else null
    }

    override fun isUserLogged(): Boolean {
        return getUser() != null
    }
}