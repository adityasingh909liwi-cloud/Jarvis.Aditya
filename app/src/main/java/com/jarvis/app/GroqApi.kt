package com.jarvis.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

// 1. Data Models for Groq API
data class Message(
    val role: String,
    val content: String
)

data class GroqRequest(
    val model: String = "llama-3.1-8b-instant",
    val messages: List<Message>,
    val temperature: Double = 0.7
)

data class GroqResponse(
    val choices: List<Choice>
) {
    data class Choice(
        val message: Message
    )
}

// 2. Retrofit API Interface
interface GroqApi {
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(
        @Header("Authorization") auth: String = "Bearer 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",
        @Body request: GroqRequest
    ): GroqResponse
}

// 3. Retrofit Client Singleton
object RetrofitClient {
    private const val BASE_URL = "https://api.groq.com/openai/"

    val instance: GroqApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApi::class.java)
    }
}
