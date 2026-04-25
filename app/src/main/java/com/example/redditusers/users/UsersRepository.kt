package com.example.redditusers.users

import com.example.redditusers.api.APIService
import javax.inject.Inject

class UsersRepository @Inject constructor(private val apiService: APIService) {

    suspend fun getUsers() = apiService.getUsers(1, 20, "desc", "reputation", "stackoverflow")

}