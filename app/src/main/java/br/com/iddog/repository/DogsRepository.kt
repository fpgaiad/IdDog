package br.com.iddog.repository

import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import retrofit2.Response

interface DogsRepository {
    suspend fun login(email: String): Response<LoginResponse>

    suspend fun getDogs(token: String, category: String): Response<FeedResponse>
}