package com.example.redditusers.di

import android.content.Context
import androidx.room.Room
import com.example.redditusers.db.FollowedUsersDao
import com.example.redditusers.db.UsersDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideUsersDatabase(@ApplicationContext context: Context): UsersDatabase =
        Room.databaseBuilder(
            context,
            UsersDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration(false).build()

    @Provides
    fun provideFollowedUsersDao(usersDatabase: UsersDatabase): FollowedUsersDao = usersDatabase.getFollowedUsersDao()
}
