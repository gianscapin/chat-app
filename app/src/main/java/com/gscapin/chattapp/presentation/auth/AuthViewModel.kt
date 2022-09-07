package com.gscapin.chattapp.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.gscapin.chattapp.core.Result
import com.gscapin.chattapp.domain.auth.AuthRepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepoImpl): ViewModel() {

    fun signIn(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repository.signIn(email, password)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }

    fun signUp(email: String, password: String, nickname: String) = liveData(Dispatchers.IO) {
        emit(Result.Loading())
        try {
            emit(Result.Success(repository.signUp(email, password, nickname)))
        }catch (e: Exception){
            emit(Result.Failure(e))
        }
    }
}