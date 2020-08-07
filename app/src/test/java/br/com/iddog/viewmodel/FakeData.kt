package br.com.iddog.viewmodel

import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import retrofit2.Response

object FakeData {
    private val gson = Gson()

    const val HUSKY = "husky"
    const val LABRADOR = "labrador"
    const val HOUND = "hound"
    const val PUG = "pug"
    const val HUSKY_FAKE_ITEM_LIST = "https://images.dog.ceo/breeds/husky/n02110185_9975.jpg"
    const val LABRADOR_FAKE_ITEM_LIST = "https://images.dog.ceo/breeds/husky/n02110185_9975.jpg"
    const val HOUND_FAKE_ITEM_LIST = "https://images.dog.ceo/breeds/husky/n02110185_9975.jpg"
    const val PUG_FAKE_ITEM_LIST = "https://images.dog.ceo/breeds/husky/n02110185_9975.jpg"

    const val FAKE_TOKEN = "123456abcdefg"
    const val FAKE_EMAIL = "fake@email.com"

    fun getFakeErrorLoginResponse(): Response<LoginResponse> {
        return Response.error(
            400,
            ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"error\":{\"message\":\"Email is not valid\"}}"
            )
        )
    }

    fun getFakeSuccessLoginResponse(): Response<LoginResponse> {
        val json = "{\n" +
                "    \"user\": {\n" +
                "        \"email\": \"$FAKE_EMAIL\",\n" +
                "        \"_id\": \"5f2b4ee78eaa972a17bd240d\",\n" +
                "        \"token\": \"$FAKE_TOKEN\",\n" +
                "        \"createdAt\": \"2020-08-06T00:29:27.927Z\",\n" +
                "        \"updatedAt\": \"2020-08-06T00:29:27.927Z\",\n" +
                "        \"__v\": 0\n" +
                "    }\n" +
                "}"
        val fakeLoginResponse = gson.fromJson(json, LoginResponse::class.java)
        return Response.success(fakeLoginResponse)
    }

    fun getFakeErrorFeedResponse(): Response<FeedResponse> {
        return Response.error(
            401,
            ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                "{\"error\":{\"message\":\"Unauthorized\"}}"
            )
        )
    }

    fun getFakeSuccessFeedResponse(dogCategory: String): Response<FeedResponse> {
        val json: String? = when (dogCategory) {
            HUSKY -> getHuskyJson()
            LABRADOR -> getLabradorJson()
            HOUND -> getHoundJson()
            PUG -> getPugJson()
            else -> null
        }
        val fakeFeedResponse = gson.fromJson(json, FeedResponse::class.java)
        return Response.success(fakeFeedResponse)
    }

    private fun getHuskyJson(): String {
        return "{\n" +
                "    \"category\": \"husky\",\n" +
                "    \"list\": [\n" +
                "        \"$HUSKY_FAKE_ITEM_LIST\"\n" +
                "    ]\n" +
                "}"
    }

    private fun getLabradorJson(): String {
        return "{\n" +
                "    \"category\": \"labrador\",\n" +
                "    \"list\": [\n" +
                "        \"$LABRADOR_FAKE_ITEM_LIST\"\n" +
                "    ]\n" +
                "}"
    }

    private fun getHoundJson(): String {
        return "{\n" +
                "    \"category\": \"hound\",\n" +
                "    \"list\": [\n" +
                "        \"$HOUND_FAKE_ITEM_LIST\"\n" +
                "    ]\n" +
                "}"
    }

    private fun getPugJson(): String {
        return "{\n" +
                "    \"category\": \"pug\",\n" +
                "    \"list\": [\n" +
                "        \"$PUG_FAKE_ITEM_LIST\"\n" +
                "    ]\n" +
                "}"
    }
}