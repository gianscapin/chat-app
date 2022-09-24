package com.gscapin.chattapp.domain.chat

import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.data.model.Message
import javax.inject.Singleton

@Singleton
interface ChatRepo {

    suspend fun getMessages(idChat: String): List<Message>
    suspend fun sendMessage(text: String, idChat: String)
    suspend fun getLatestMessage(idChat: String): Message?
}