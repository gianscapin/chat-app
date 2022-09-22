package com.gscapin.chattapp.domain.contact

import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.data.remote.contact.ContactDatasource
import javax.inject.Inject

class ContactRepoImpl @Inject constructor(private val datasource: ContactDatasource): ContactRepo {
    override suspend fun getContactList() = datasource.getContactListFromUser()
    override suspend fun getUsers(): List<User> = datasource.getUsers()
    override suspend fun addChat(user: User): ContactMessage = datasource.createChat(user = user)
    override suspend fun deleteChat(contactMessage: ContactMessage): Boolean = datasource.deleteContact(contactMessage)
}