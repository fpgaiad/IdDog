package br.com.iddog.data.network

import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.util.NetworkConstants
import retrofit2.Response
import retrofit2.http.*

interface DogsApi : WebApi {

    @FormUrlEncoded
    @POST(NetworkConstants.URL_ACTION_LOGIN)
    @Headers("Content-Type: application/x-www-form-urlencoded")
    override suspend fun login(
        @Field(
            value = "email",
            encoded = true
        ) email: String
    ): Response<LoginResponse>

    @GET(NetworkConstants.URL_ACTION_FEED)
    @Headers("Content-Type: application/json")
    override suspend fun getDogs(
        @Header("Authorization") token: String,
        @Query("category") category: String
    ): Response<FeedResponse>
}