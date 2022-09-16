package com.gscapin.chattapp.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.data.model.Chat
import com.gscapin.chattapp.data.model.Message
import com.gscapin.chattapp.domain.chat.ChatRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepoImpl): ViewModel() {

    private val messagesState = MutableStateFlow<Result<List<Message>>>(Result.Loading())

    fun getMessages(idChat:String) = viewModelScope.launch {
        kotlin.runCatching {
            getListToState(idChat)
        }.onFailure {
            messagesState.value = Result.Failure(Exception(it.message))
        }
    }

    private fun getListToState(idChat: String) =
        FirebaseFirestore.getInstance().collection("chat").document(idChat)
            .addSnapshotListener { snapshot, e ->
                val listMessages: MutableList<Message> = mutableListOf()
                val chat = snapshot!!.toObject(Chat::class.java)
                for (messageChat in chat?.text!!) {
                    listMessages.add(messageChat)
                }
                messagesState.value = Result.Success(listMessages)
            }

    fun getListMessages(): StateFlow<Result<List<Message>>> = messagesState

    fun sendMessage(text: String, idChat: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repository.sendMessage(text, idChat)))
        } catch (e: Exception){
            emit(Result.Failure(e))
        }
    }
}