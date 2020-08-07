package br.com.iddog.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.data.model.login.User
import br.com.iddog.data.store.Store
import br.com.iddog.repository.DogsRepository
import br.com.iddog.util.ViewModelConstants
import br.com.iddog.viewmodel.FakeData.FAKE_EMAIL
import br.com.iddog.viewmodel.FakeData.FAKE_TOKEN
import br.com.iddog.viewmodel.FakeData.HUSKY
import br.com.iddog.viewmodel.FakeData.HUSKY_FAKE_ITEM_LIST
import br.com.iddog.viewmodel.FakeData.LABRADOR
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DogsViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private lateinit var dogsViewModel: DogsViewModel
    private lateinit var emailToLogin: String

    @Mock
    private lateinit var mockDogsRepository: DogsRepository

    @Mock
    private lateinit var mockUserStore: Store

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        dogsViewModel = DogsViewModel(mockDogsRepository, mockUserStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `when call login and get success response should update user property with success response`() {
        val fakeSuccessLoginResponse = FakeData.getFakeSuccessLoginResponse()
        runBlocking {
            `when`(mockDogsRepository.login(FAKE_EMAIL)).thenReturn(fakeSuccessLoginResponse)
        }

        dogsViewModel.login(FAKE_EMAIL)

        assertEquals(LoginResponse(User(FAKE_EMAIL, FAKE_TOKEN)), dogsViewModel.user.value?.data)
    }

    @Test
    fun `when call login and get error response should update user property with error response`() {
        val fakeErrorLoginResponse = FakeData.getFakeErrorLoginResponse()
        runBlocking {
            `when`(mockDogsRepository.login(FAKE_EMAIL)).thenReturn(fakeErrorLoginResponse)
        }

        dogsViewModel.login(FAKE_EMAIL)

        assertEquals(ViewModelConstants.GENERIC_ERROR, dogsViewModel.user.value?.message)
    }


    @Test
    fun `when call getDogsByCategory and get success response should update dogs property with success response`() {
        val dogCategory = HUSKY
        val fakeSuccessFeedResponse = FakeData.getFakeSuccessFeedResponse(dogCategory)
        runBlocking {
            `when`(mockDogsRepository.getDogs(FAKE_TOKEN, dogCategory)).thenReturn(
                fakeSuccessFeedResponse
            )
            `when`(mockUserStore.getUser()).thenReturn(User(FAKE_EMAIL, FAKE_TOKEN))
        }

        dogsViewModel.getDogsByCategory(dogCategory)

        assertEquals(
            FeedResponse(dogCategory, listOf(HUSKY_FAKE_ITEM_LIST)),
            dogsViewModel.dogs.value?.data
        )
    }

    @Test
    fun `when call getDogsByCategory and get error response should update dogs property with error response`() {
        val dogCategory = LABRADOR
        val fakeErrorFeedResponse = FakeData.getFakeErrorFeedResponse()
        runBlocking {
            `when`(mockDogsRepository.getDogs(FAKE_TOKEN, dogCategory)).thenReturn(
                fakeErrorFeedResponse
            )
            `when`(mockUserStore.getUser()).thenReturn(User(FAKE_EMAIL, FAKE_TOKEN))
        }

        dogsViewModel.getDogsByCategory(dogCategory)

        assertEquals(
            ViewModelConstants.UNAUTHORIZED,
            dogsViewModel.dogs.value?.message
        )
    }
}
