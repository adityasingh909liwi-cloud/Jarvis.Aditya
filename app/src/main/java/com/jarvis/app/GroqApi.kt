package com.jarvis.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

data class Message(
    val role: String,
    val content: String
)

data class GroqRequest(
    val model: String = "llama-3.1-8b-instant",
    val messages: List<Message>
)

data class Choice(
    val message: Message
)

data class GroqResponse(
    val choices: List<Choice>
)

interface GroqApiService {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") authHeader: String,
        @Body request: GroqRequest
    ): GroqResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://api.groq.com/openai/"

    val instance: GroqApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApiService::class.java)
    }
}
