package com.jarvis.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class JarvisViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _uiState = MutableStateFlow<ChatUiState>(ChatUiState.Idle)
    val uiState: StateFlow<ChatUiState> = _uiState

    fun sendMessage(userText: String) {
        if (userText.isBlank()) return

        val currentList = _messages.value.toMutableList()
        currentList.add(ChatMessage(content = userText, isUser = true))
        _messages.value = currentList
        _uiState.value = ChatUiState.Loading

        viewModelScope.launch {
            try {
                // System message से JARVIS पर्सनालिटी सेट होगी
                val apiMessages = mutableListOf(
                    Message(role = "system", content = "You are JARVIS, a highly intelligent and helpful AI assistant.")
                )
                
                // पुरानी चैट हिस्ट्री API को पास करना
                apiMessages.addAll(currentList.map { 
                    Message(role = if (it.isUser) "user" else "assistant", content = it.content) 
                })

                val request = GroqRequest(messages = apiMessages)
                val response = RetrofitClient.instance.getChatCompletion(request = request)
                
                val aiReply = response.choices.firstOrNull()?.message?.content ?: "No response from JARVIS."
                
                currentList.add(ChatMessage(content = aiReply, isUser = false))
                _messages.value = currentList
                _uiState.value = ChatUiState.Success(currentList)

            } catch (e: Exception) {
                _uiState.value = ChatUiState.Error(e.localizedMessage ?: "Unknown Error")
            }
        }
    }
}
