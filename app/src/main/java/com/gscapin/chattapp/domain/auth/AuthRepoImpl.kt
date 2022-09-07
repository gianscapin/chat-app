package com.gscapin.chattapp.domain.auth

import com.gscapin.chattapp.data.remote.auth.AuthDatasource
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(private val dataSource: AuthDatasource): AuthRepo {
    override suspend fun signIn(email: String, password: String) = dataSource.signIn(email, password)

    override suspend fun signUp(email: String, password: String, nickname: String) = dataSource.signUp(email, password, nickname)
}