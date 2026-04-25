package com.example.redditusers.users

import androidx.lifecycle.ViewModel
import com.example.redditusers.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(private val usersRepository: UsersRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> get() = _uiState

    suspend fun getUsers() {
        try {
            val response = usersRepository.getUsers()
            _uiState.value = UIState.Success(response.items)
        } catch(e: Exception) {
            _uiState.value = UIState.Error(e.localizedMessage)
        }
    }
}