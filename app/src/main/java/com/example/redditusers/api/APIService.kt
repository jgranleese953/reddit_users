package com.example.redditusers.api

import com.example.redditusers.model.User
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {

    @GET("/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("order") order: String,
        @Query("sort") sort: String,
        @Query("site") site: String): User

}