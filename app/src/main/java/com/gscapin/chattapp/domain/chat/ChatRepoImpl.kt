package com.gscapin.chattapp.domain.chat

import com.gscapin.chattapp.data.model.Message
import com.gscapin.chattapp.data.remote.chat.ChatDatasource
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(private val datasource: ChatDatasource): ChatRepo {
    override suspend fun getMessages(idChat: String): List<Message> = datasource.getMessages(idChat)
    override suspend fun sendMessage(text: String, idChat: String) = datasource.sendMessage(text, idChat)
    override suspend fun getLatestMessage(idChat: String): Message? = datasource.getLatestText(idChat)
}