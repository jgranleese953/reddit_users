package com.example.redditusers.ui.utils

sealed class UIState<out T> {
    object Loading: UIState<Nothing>()
    data class Success<T>(val result: T): UIState<T>()
    data class Error(val message: String?): UIState<Nothing>()
}