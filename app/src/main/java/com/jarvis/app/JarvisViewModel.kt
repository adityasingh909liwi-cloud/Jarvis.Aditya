package com.jarvis.app

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class JarvisViewModel : ViewModel() {

    val messages = mutableStateListOf<ChatMessage>()

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        // 1. यूजर का मैसेज जोड़ें
        messages.add(ChatMessage(text = userText, isUser = true))

        // 2. Groq API कॉल करें
        viewModelScope.launch {
            try {
                val apiMessages = messages.map { Message(role = if (it.isUser) "user" else "assistant", content = it.text) }
                val request = GroqRequest(messages = apiMessages)
                val response = RetrofitClient.instance.getChatCompletion(request)
                
                val botReply = response.choices.firstOrNull()?.message?.content ?: "No response from JARVIS."
                messages.add(ChatMessage(text = botReply, isUser = false))
            } catch (e: Exception) {
                messages.add(ChatMessage(text = "Error: ${e.localizedMessage ?: "Unknown network error"}", isUser = false))
            }
        }
    }
}
