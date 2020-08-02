package br.com.iddog.data.network

import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface DogsApi {

    @POST(NetworkConstants.URL_ACTION_LOGIN)
    @Headers("Content-Type: application/json")
    suspend fun login(@Body email: String): Response<LoginResponse>

    @GET(NetworkConstants.URL_ACTION_FEED)
    @Headers("Content-Type: application/json")
    suspend fun getDogs(@Query("category") category: String): Response<FeedResponse>
}