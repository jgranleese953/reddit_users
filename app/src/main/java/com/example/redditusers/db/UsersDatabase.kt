package com.example.redditusers.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.redditusers.model.FollowedUser

@Database(entities = [(FollowedUser::class)], version = 1)
abstract class UsersDatabase: RoomDatabase() {

    abstract fun getFollowedUsersDao(): FollowedUsersDao

}