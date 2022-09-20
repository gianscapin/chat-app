package com.gscapin.chattapp.presentation.contact

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.data.model.User
import com.gscapin.chattapp.domain.contact.ContactRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(private val repository: ContactRepoImpl): ViewModel() {

    fun getContactList() = liveData(Dispatchers.IO) {
        emit(Result.Loading())

        kotlin.runCatching {
            repository.getContactList()
        }.onSuccess { result ->
            emit(Result.Success(result))
        }.onFailure {
            emit(Result.Failure(Exception(it.message)))
        }
    }

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
}