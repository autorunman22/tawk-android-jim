package com.tawkto.jim.repository

import app.cash.turbine.test
import com.tawkto.jim.db.UserCacheMapper
import com.tawkto.jim.db.UserDao
import com.tawkto.jim.retrofit.GithubService
import com.tawkto.jim.retrofit.NetworkMapper
import com.tawkto.jim.util.DataState
import com.tawkto.jim.util.TestUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.*
import timber.log.Timber
import java.util.*
import kotlin.time.ExperimentalTime

class UserRepositoryTest {
    private lateinit var userRepository: UserRepository
    private val service = mock(GithubService::class.java)
    private val networkMapper = mock(NetworkMapper::class.java)
    private val userDao = mock(UserDao::class.java)
    private val userCacheMapper = mock(UserCacheMapper::class.java)

    @Before
    fun setUp() {
        userRepository = UserRepository(service, networkMapper, userDao, userCacheMapper)
    }

    @ExperimentalCoroutinesApi
    @ExperimentalTime
    @Test
    fun getUsers() {
        // Does not respect delay
        TestCoroutineScope(TestCoroutineDispatcher()).launch {
            val user = TestUtil.getTestUser()
            val users = listOf(TestUtil.getTestUser())
            val userCacheEntity = TestUtil.getTestUserCacheEntity()

            `when`(userCacheMapper.mapFromEntityList(anyList())).thenReturn(users)
            `when`(userCacheMapper.mapToEntity(user)).thenReturn(userCacheEntity)
            `when`(networkMapper.mapFromEntityList(anyList(), anyList())).thenReturn(users)
            `when`(userDao.insert(TestUtil.getTestUserCacheEntity())).thenReturn(1)

            userRepository.getUsers().test {
                assertSame(DataState.Loading, expectItem())
                assertEquals(DataState.Success(users), expectItem())
                assertEquals(DataState.Success(users), expectItem())
                cancelAndConsumeRemainingEvents()
            }
            verify(userDao).users()
            verify(service).users()
        }
    }

    @Test
    fun `given repository, when updateNoteById, verify that updateNoteById was called on userDao`() {
        // Given a note
        userRepository.updateNoteById(anyInt(), anyString())

        // Verify that updateNoteById was called
        verify(userDao).updateNoteById(anyInt(), anyBoolean())
    }

    @Test
    fun `given repository, when updateNoteById, verify that updateNoteById note param received true`() {
        // Given non-null note
        userRepository.updateNoteById(1, "simple note")

        val intCaptor = ArgumentCaptor.forClass(Int::class.java)
        val boolCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(userDao).updateNoteById(intCaptor.capture(), boolCaptor.capture())

        assertEquals(1, intCaptor.value)
        assertTrue(boolCaptor.value)
    }

    @Test
    fun `given repository, when updateNoteById, verify that updateNoteById note param received false`() {
        // Given non-null note
        userRepository.updateNoteById(1, null)

        val intCaptor = ArgumentCaptor.forClass(Int::class.java)
        val boolCaptor = ArgumentCaptor.forClass(Boolean::class.java)
        verify(userDao).updateNoteById(intCaptor.capture(), boolCaptor.capture())

        assertEquals(1, intCaptor.value)
        assertFalse(boolCaptor.value)
    }
}