package com.example.redditusers.users

import androidx.annotation.VisibleForTesting
import com.example.redditusers.api.APIService
import com.example.redditusers.db.FollowedUsersDao
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UsersRepository @Inject constructor(
    private val apiService: APIService,
    private val followedUsersDao: FollowedUsersDao
) {

    @VisibleForTesting
    val users = flow {
        emit(apiService.getUsers(1, 20, "desc", "reputation", "stackoverflow"))
    }

    @VisibleForTesting
    val followedUsers = followedUsersDao.getFollowedUsers()

    val state = users.combine(followedUsers) { users, favourites ->
        val favouriteIds = favourites.map { it.userId }.toSet()
        users.items.map { userDetails ->
            userDetails.copy(isFollowed = favouriteIds.contains(userDetails.userId))
        }
    }

    suspend fun toggleFavourite(userId: String, isSelected: Boolean) {
        if(isSelected) {
            followedUsersDao.removeFollowUser(userId)
        } else followedUsersDao.addFollowUser(userId)
    }
}