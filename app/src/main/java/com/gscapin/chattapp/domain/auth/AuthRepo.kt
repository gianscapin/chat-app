package com.gscapin.chattapp.domain.auth

import javax.inject.Singleton

@Singleton
interface AuthRepo {
    suspend fun signIn(email: String, password: String)
    suspend fun signUp(email: String, password: String, nickname: String)
}