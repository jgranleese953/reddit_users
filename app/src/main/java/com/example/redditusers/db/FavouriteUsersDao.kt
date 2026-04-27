package com.example.redditusers.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.redditusers.model.FollowedUser
import kotlinx.coroutines.flow.Flow

@Dao
interface FollowedUsersDao {

    @Insert
    suspend fun insert(favouriteUser: FollowedUser)

    @Query("SELECT * from followed_users_table")
    fun getFollowedUsers(): Flow<List<FollowedUser>>

    suspend fun addFollowUser(userId: String) {
        insert(FollowedUser(userId))
    }

    @Query("DELETE FROM followed_users_table WHERE userId = :userId")
    suspend fun removeFollowUser(userId: String)

}