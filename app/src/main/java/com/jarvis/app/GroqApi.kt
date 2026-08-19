package com.jarvis.app

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

data class Message(val role: String, val content: String)
data class GroqRequest(
    val model: String = "llama-3.1-8b-instant",
    val messages: List<Message>
)
data class GroqResponse(val choices: List<Choice>)
data class Choice(val message: Message)

interface GroqApi {
    @Headers(
        "Authorization: Bearer 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",
        "Content-Type: application/json"
    )
    @POST
    suspend fun getChatCompletion(@Url url: String = "https://api.groq.com/openai/v1/chat/completions", @Body request: GroqRequest): GroqResponse
}

object RetrofitClient {
    val instance: GroqApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.groq.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GroqApi::class.java)
    }
}
