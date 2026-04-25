package com.example.redditusers.users

import com.example.redditusers.MainDispatcherRule
import com.example.redditusers.model.User
import com.example.redditusers.model.UserDetails
import com.example.redditusers.utils.UIState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
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

    lateinit var usersViewModel: UsersViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        usersViewModel = spyk(UsersViewModel(usersRepository))
    }

    @Test
    fun `test initial state`() = runTest {
        advanceUntilIdle()

        assertTrue(usersViewModel.uiState.value is UIState.Loading)
    }

    @Test
    fun `test make api call success`() = runTest {
        val users = mockk<User>()
        val items = mockk<List<UserDetails>>()

        coEvery { usersRepository.getUsers() } returns users
        every { users.items } returns items

        usersViewModel.getUsers()
        advanceUntilIdle()

        assertEquals(items, (usersViewModel.uiState.value as UIState.Success).result)
    }

    @Test
    fun `test make api call failure`() = runTest {
        val localisedMessage = "message"
        val exception = mockk<Exception>()

        coEvery { usersRepository.getUsers() } throws exception
        every { exception.localizedMessage } returns localisedMessage

        usersViewModel.getUsers()
        advanceUntilIdle()

        assertEquals(localisedMessage, (usersViewModel.uiState.value as UIState.Error).message)
    }
}