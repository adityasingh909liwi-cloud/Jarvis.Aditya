package com.jarvis.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

// डेटा मॉडल
data class Message(
    val role: String,
    val content: String
)

data class GroqRequest(
    val model: String = "llama3-8b-8192",
    val messages: List<Message>
)

data class Choice(
    val message: Message
)

data class GroqResponse(
    val choices: List<Choice>
)

// Retrofit API इंटरफेस
interface GroqApiService {
    @Headers(
        "Authorization: Bearer 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",
        "Content-Type: application/json"
    )
    @POST("v1/chat/completions")
    suspend fun getChatCompletion(@Body request: GroqRequest): GroqResponse
}

// Retrofit singleton क्लाइंट
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
