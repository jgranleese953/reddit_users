package com.example.redditusers.users

import com.example.redditusers.api.APIService
import com.example.redditusers.db.FollowedUsersDao
import com.example.redditusers.model.FollowedUser
import com.example.redditusers.model.User
import com.example.redditusers.model.UserDetails
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class UsersRepositoryTest {

    @MockK
    lateinit var apiService: APIService

    @MockK
    lateinit var followedUsersDao: FollowedUsersDao

    @MockK
    lateinit var followedUsersFlow: Flow<List<FollowedUser>>

    @MockK
    lateinit var users: User

    lateinit var usersRepository: UsersRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        coEvery { apiService.getUsers(1, 20, "desc", "reputation", "stackoverflow") } returns users
        coEvery { followedUsersDao.getFollowedUsers() } returns followedUsersFlow

        usersRepository = spyk(UsersRepository(apiService, followedUsersDao))
    }

    @Test
    fun `test users`() = runTest {
        usersRepository.users.collect {
            assertEquals(users, it)
        }
    }

    @Test
    fun `test get state`() = runTest {
        val user1: UserDetails = mockk()
        val user2: UserDetails = mockk()
        val user3: UserDetails = mockk()

        every { user1.userId } returns "user1"
        every { user2.userId } returns "user2"
        every { user3.userId } returns "user3"

        val followedUser: FollowedUser = mockk()

        every { followedUser.userId } returns "user3"

        every { users.items } returns listOf(user1, user2, user3)

        coEvery { followedUsersDao.getFollowedUsers() } returns flowOf(listOf(followedUser))

       usersRepository = UsersRepository(apiService, followedUsersDao)

        every { user1.copy(isFollowed = false) } returns user1
        every { user2.copy(isFollowed = false) } returns user2
        every { user3.copy(isFollowed = true) } returns user3

        usersRepository.state.collect {

        }

        verify { user1.copy(isFollowed = false) }
        verify { user2.copy(isFollowed = false) }
        verify { user3.copy(isFollowed = true)  }
    }

    @Test
    fun `test toggle favourite`() = runTest {
        val userId = "userId"

        coEvery { followedUsersDao.addFollowUser(userId) } returns Unit
        coEvery { followedUsersDao.removeFollowUser(userId) } returns Unit

        usersRepository.toggleFavourite(userId, true)

        coVerify { followedUsersDao.removeFollowUser(userId) }
        coVerify(exactly = 0) { followedUsersDao.addFollowUser(userId) }
    }

    @Test
    fun `test toggle favourite off`() = runTest {
        val userId = "userId"

        coEvery { followedUsersDao.addFollowUser(userId) } returns Unit
        coEvery { followedUsersDao.removeFollowUser(userId) } returns Unit

        usersRepository.toggleFavourite(userId, false)

        coVerify(exactly = 0) { followedUsersDao.removeFollowUser(userId) }
        coVerify { followedUsersDao.addFollowUser(userId) }
    }
}