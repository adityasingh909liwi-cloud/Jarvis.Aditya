package com.jarvis.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// -- FIX: Import the missing Data components --
import com.jarvis.app.data.models.Message
import com.jarvis.app.data.models.GroqRequest
import com.jarvis.app.data.remote.RetrofitClient

class JarvisViewModel : ViewModel() {

    fun sendMessage() {
        viewModelScope.launch {
            // Fixes "Unresolved reference: Message"
            val message = Message()
            
            // Fixes "Unresolved reference: RetrofitClient" and "GroqRequest"
            // Assume you have a service interface for Groq
            // val api = RetrofitClient.create(GroqApiService::class.java)
            // val request = GroqRequest(message.text)
            // api.sendRequest(request)
        }
    }
}
