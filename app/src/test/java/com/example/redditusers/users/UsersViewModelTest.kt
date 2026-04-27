package com.example.redditusers.users

import com.example.redditusers.MainDispatcherRule
import com.example.redditusers.model.UserDetails
import com.example.redditusers.ui.utils.UIState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    @MockK
    private lateinit var usersRepository: UsersRepository

    private lateinit var usersViewModel: UsersViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        usersViewModel = UsersViewModel(usersRepository)
    }

    @Test
    fun `test initial state`() = runTest {
        assertTrue(usersViewModel.uiState.value is UIState.Loading)
    }

    @Test
    fun `test get users success`() = runTest {
        val users = listOf(mockk<UserDetails>())
        val flow = flow { emit(users) }

        every { usersRepository.state } returns flow

        usersViewModel.getUsers()
        advanceUntilIdle()

        val currentState = usersViewModel.uiState.value
        assertEquals((currentState as UIState.Success).result, users)
    }

    @Test
    fun `test get users failure`() = runTest {
        val errorMessage = "error"
        val flow = flow<List<UserDetails>> { throw Exception(errorMessage) }

        every { usersRepository.state } returns flow

        usersViewModel.getUsers()
        advanceUntilIdle()

        val currentState = usersViewModel.uiState.value
        assertTrue(currentState is UIState.Error)
        assertEquals((currentState as UIState.Error).message, errorMessage)
    }

    @Test
    fun `test toggleFavourite calls repository`() = runTest {
        val userId = "userId"
        val isSelected = true

        coEvery { usersRepository.toggleFavourite(userId, isSelected) } returns Unit

        usersViewModel.toggleFavourite(userId, isSelected)
        advanceUntilIdle()

        coVerify { usersRepository.toggleFavourite(userId, isSelected) }
    }
}
