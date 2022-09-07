package com.gscapin.chattapp.di

import com.gscapin.chattapp.data.remote.auth.AuthDatasource
import com.gscapin.chattapp.domain.auth.AuthRepoImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    fun provideAuthRepository(datasource: AuthDatasource) = AuthRepoImpl(datasource)
}