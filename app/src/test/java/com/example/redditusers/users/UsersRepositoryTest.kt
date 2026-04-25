package com.example.redditusers.users

import com.example.redditusers.api.APIService
import com.example.redditusers.model.User
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class UsersRepositoryTest {

    @MockK
    lateinit var apiService: APIService

    lateinit var usersRepository: UsersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        usersRepository = spyk(UsersRepository(apiService))
    }

    @Test
    fun `test get users`() = runBlocking {
        val user = mockk<User>()
        coEvery { apiService.getUsers(1, 20, "desc", "reputation", "stackoverflow") } returns user

        assertEquals(usersRepository.getUsers(), user)
    }
}