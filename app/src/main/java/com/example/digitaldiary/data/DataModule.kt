package com.example.digitaldiary.data

import com.example.digitaldiary.domain.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideNoteRepository(): NoteRepository {
        return NoteRepositoryImpl()
    }
}