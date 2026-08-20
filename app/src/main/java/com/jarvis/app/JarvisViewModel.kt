package com.jarvis.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JarvisViewModel : ViewModel() {

    val messages = mutableStateListOf<ChatMessage>()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        messages.add(ChatMessage(content = userText, isUser = true))

        viewModelScope.launch {
            try {
                val apiMessages = messages.map { Message(role = if (it.isUser) "user" else "assistant", content = it.content) }
                val request = GroqRequest(messages = apiMessages)
                val authHeader = "Bearer " + BuildConfig.GROQ_API_KEY
                
                val response = RetrofitClient.instance.getChatCompletion(authHeader, request)
                
                val botReply = response.choices.firstOrNull()?.message?.content ?: "No response from JARVIS."
                messages.add(ChatMessage(content = botReply, isUser = false))
            } catch (e: Exception) {
                messages.add(ChatMessage(content = "Error: ${e.localizedMessage ?: "Unknown network error"}", isUser = false))
            }
        }
    }
}
