package com.gscapin.chattapp.presentation.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.data.model.Chat
import com.gscapin.chattapp.data.model.ContactMessage
import com.gscapin.chattapp.data.model.Message
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.domain.contact.ContactRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: ContactRepoImpl) : ViewModel() {

    private val contactsState = MutableStateFlow<Result<List<ContactMessage>>>(Result.Loading())

    fun getContactList() = viewModelScope.launch {
        kotlin.runCatching {
            getContacts()
        }.onFailure {
            contactsState.value = (Result.Failure(Exception(it.message)))
        }
    }

    private fun getContacts() {
        val currentUserId = FirebaseAuth.getInstance().currentUser!!.uid

        FirebaseFirestore.getInstance().collection("users").document(currentUserId)
            .addSnapshotListener { snapshot, error ->
                val listContacts: MutableList<ContactMessage> = mutableListOf()
                val user = snapshot!!.toObject(User::class.java)
                for (contact in user?.contacts!!) {
                    listContacts.add(contact)
                }
                contactsState.value = Result.Success(listContacts)
            }

    }

    fun getListContacts(): StateFlow<Result<List<ContactMessage>>> = contactsState


    fun getUsers() = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        kotlin.runCatching {
            repository.getUsers()
        }.onSuccess { result ->
            emit(Result.Success(result))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }

    suspend fun addChat(user: User) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        kotlin.runCatching {
            repository.addChat(user)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }

    suspend fun deleteChat(contactMessage: ContactMessage) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        kotlin.runCatching {
            repository.deleteChat(contactMessage)
        }.onSuccess {
            emit(Result.Success(it))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }
}