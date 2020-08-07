package br.com.iddog.data.store

import br.com.iddog.data.model.login.User

interface Store {

    fun saveUser(email: String, token: String)

    fun getUser(): User?
}