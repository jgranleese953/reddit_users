package com.example.redditusers.api

import com.example.redditusers.model.User
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface APIService {

    @GET("/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("order") order: String,
        @Query("sort") sort: String,
        @Query("site") site: String): User
}