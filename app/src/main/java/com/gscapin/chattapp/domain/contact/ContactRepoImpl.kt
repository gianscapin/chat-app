package com.gscapin.chattapp.domain.contact

import com.gscapin.chattapp.data.remote.contact.ContactDatasource
import javax.inject.Inject

class ContactRepoImpl @Inject constructor(private val datasource: ContactDatasource): ContactRepo {
    override suspend fun getContactList() = datasource.getContactListFromUser()
}