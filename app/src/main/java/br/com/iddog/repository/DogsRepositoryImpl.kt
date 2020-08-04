package br.com.iddog.repository

import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.data.network.WebApi
import retrofit2.Response
import javax.inject.Inject

class DogsRepositoryImpl @Inject constructor(
    private val api: WebApi
) : DogsRepository {

    override suspend fun login(email: String): Response<LoginResponse> {
        return api.login(email)
    }

    override suspend fun getDogs(token: String, category: String) = api.getDogs(token, category)
}