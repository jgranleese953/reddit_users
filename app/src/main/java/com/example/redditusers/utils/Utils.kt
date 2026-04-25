package com.example.redditusers.utils

import com.example.redditusers.model.UserDetails

class Utils {

    companion object {
        const val BASE_URL = "http://api.stackexchange.com/2.2/"
    }
}

sealed class UIState {
    object Loading: UIState()
    data class Success(val result: List<UserDetails>): UIState()
    data class Error(val message: String?): UIState()
}