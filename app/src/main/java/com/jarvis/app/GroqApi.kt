package com.jarvis.app

// -- FIX: Import the missing Koin and Data components --
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.jarvis.app.data.models.Message
import com.jarvis.app.data.models.GroupRequest
import com.jarvis.app.data.remote.RetrofitClient
import com.jarvis.app.data.services.GroupService

class GroqApi : KoinComponent {
    // This 'by inject()' will now work because of KoinComponent and the Gradle dependency
    private val groupService: GroupService by inject()

    fun fetchData(): List<Message> {
        return groupService.getMessages() // Example call
    }

    fun sendGroupRequest() {
        val request = GroupRequest()
        // Fixes RetrofitClient unresolved reference
        val service = RetrofitClient.create(GroupService::class.java) 
        // service.create(request) 
    }
}
