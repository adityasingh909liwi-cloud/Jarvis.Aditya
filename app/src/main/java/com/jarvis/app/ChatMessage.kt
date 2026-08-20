package com.jarvis.app

// UI में मैसेज दिखाने के लिए डेटा क्लास
data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

// UI की अलग-अलग स्थितियों (State) को हैंडल करने के लिए
sealed interface ChatUiState {
    object Idle : ChatUiState
    object Loading : ChatUiState
    data class Success(val messages: List<ChatMessage>) : ChatUiState
    data class Error(val message: String) : ChatUiState
}
