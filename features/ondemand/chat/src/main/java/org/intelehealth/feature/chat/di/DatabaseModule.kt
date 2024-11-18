package org.intelehealth.feature.chat.di

import android.content.Context
import org.intelehealth.feature.chat.room.ChatDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context) = ChatDatabase.getInstance(context)

    @Singleton
    @Provides
    fun provideChatDao(appDatabase: ChatDatabase) = appDatabase.chatDao()
}