package com.gscapin.chattapp.domain.contact

import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.User
import javax.inject.Singleton

@Singleton
interface ContactRepo {
    suspend fun getContactList(): List<ContactMessage>
    suspend fun getUsers(): List<User>
    suspend fun addChat(user: User): ContactMessage
}