package com.example.redditusers.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.redditusers.model.UserDetails
import com.example.redditusers.ui.utils.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<List<UserDetails>>>(UIState.Loading)
    val uiState: StateFlow<UIState<List<UserDetails>>> get() = _uiState

    fun getUsers() = viewModelScope.launch {
        usersRepository.state.catch { error ->
            _uiState.value = UIState.Error(error.localizedMessage)
        }.collect { users ->
            _uiState.value = UIState.Success(users)
        }
    }

    fun toggleFavourite(userId: String, isSelected: Boolean) = viewModelScope.launch {
        usersRepository.toggleFavourite(userId, isSelected)
    }
}