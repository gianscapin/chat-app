package com.gscapin.chattapp.di

import com.gscapin.chattapp.data.remote.auth.AuthDatasource
import com.gscapin.chattapp.data.remote.chat.ChatDatasource
import com.gscapin.chattapp.data.remote.contact.ContactDatasource
import com.gscapin.chattapp.domain.auth.AuthRepoImpl
import com.gscapin.chattapp.domain.chat.ChatRepoImpl
import com.gscapin.chattapp.domain.contact.ContactRepoImpl
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    fun provideAuthRepository(datasource: AuthDatasource) = AuthRepoImpl(datasource)

    @Singleton
    fun provideContactRepository(datasource: ContactDatasource) = ContactRepoImpl(datasource)

    @Singleton
    fun provideChatRepository(datasource: ChatDatasource) = ChatRepoImpl(datasource)
}