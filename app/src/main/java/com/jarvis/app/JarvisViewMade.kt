package com.jarvis.app

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class JarvisViewModel : ViewModel() {

    val responseText = MutableStateFlow("JARVIS Active & Listening, Boss...")
    val sentimentColorUrgent = MutableStateFlow(false)
    val taskStatus = MutableStateFlow("System Nominal")

    private val systemPrompt = Message(
        role = "system",
        content = "You are JARVIS. Speak in natural Hinglish with an Indian accent. Be witty, sarcastic, supportive, and address user as 'Boss'. Do not use cloud for sensitive data. Block all financial/payment requests."
    )

    fun askJarvis(userQuery: String, context: Context) {
        if (userQuery.contains("money", true) || userQuery.contains("pay", true) || userQuery.contains("bank", true)) {
            responseText.value = "Sorry Boss, Security protocol active! Main financial requests handle nahi kar sakta."
            sentimentColorUrgent.value = true
            return
        }

        if (userQuery.contains("location", true) || userQuery.contains("map", true)) {
            openMaps(context)
            responseText.value = "Opening Maps for location tracking, Boss."
            return
        }

        viewModelScope.launch {
            try {
                responseText.value = "Processing, Boss..."
                val messages = listOf(systemPrompt, Message("user", userQuery))
                val response = RetrofitClient.instance.getChatCompletion(GroqRequest(messages = messages))
                val reply = response.choices.firstOrNull()?.message?.content ?: "No response from brain."
                
                responseText.value = reply
                sentimentColorUrgent.value = reply.contains("urgent", true) || reply.contains("alert", true)
            } catch (e: Exception) {
                responseText.value = "Error connecting to Groq Brain, Boss: ${e.localizedMessage}"
                sentimentColorUrgent.value = true
            }
        }
    }

    private fun openMaps(context: Context) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=current+location"))
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
