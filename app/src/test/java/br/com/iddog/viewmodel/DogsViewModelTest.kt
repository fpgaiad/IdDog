package br.com.iddog.viewmodel

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.data.model.login.User
import br.com.iddog.repository.DogsRepository
import br.com.iddog.util.Resource
import br.com.iddog.util.ViewModelConstants
import br.com.iddog.viewmodel.FakeData.FAKE_EMAIL
import br.com.iddog.viewmodel.FakeData.FAKE_TOKEN
import br.com.iddog.viewmodel.FakeData.HUSKY
import br.com.iddog.viewmodel.FakeData.HUSKY_FAKE_ITEM_LIST
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DogsViewModelTest {


    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    private lateinit var dogsViewModel: DogsViewModel
    private lateinit var emailToLogin: String

    @Mock
    private lateinit var mockDogsRepository: DogsRepository

    @Mock
    private lateinit var sharedPref: SharedPreferences

    @Mock
    private lateinit var userLiveDataObserver: Observer<Resource<LoginResponse>>
    @Mock
    private lateinit var dogsLiveDataObserver: Observer<Resource<FeedResponse>>

    @Mock
    private lateinit var loading: Resource.Loading<LoginResponse>


    @Before
    fun setUp() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        dogsViewModel = DogsViewModel(mockDogsRepository)
        emailToLogin = ""
    }

    @Test
    fun `when call login and get success response should update user property with success response`() {
        val fakeSuccessLoginResponse = FakeData.getFakeSuccessLoginResponse()
        setResponseForRepositoryLogin(fakeSuccessLoginResponse)

        dogsViewModel.login(FAKE_EMAIL)

        assertEquals(LoginResponse(User(FAKE_EMAIL, FAKE_TOKEN)), dogsViewModel.user.value?.data)
    }

    @Test
    fun `when call login and get error response should update user property with error response`() {
        val fakeErrorLoginResponse = FakeData.getFakeErrorLoginResponse()
        setResponseForRepositoryLogin(fakeErrorLoginResponse)

        dogsViewModel.login(FAKE_EMAIL)

        assertEquals(ViewModelConstants.GENERIC_ERROR, dogsViewModel.user.value?.message)
    }


    // TODO: ERRO!Token é sempre nulo
    @Test
    fun `when call getDogsByCategory and get success response should update user property`() {
        val dogCategory = HUSKY
        val fakeSuccessFeedResponse = FakeData.getFakeSuccessFeedResponse(dogCategory)
        setResponseForRepositoryFeed(fakeSuccessFeedResponse, dogCategory)
//        `when`(dogsViewModel.getToken()).thenReturn(FAKE_TOKEN)

        dogsViewModel.getDogsByCategory(dogCategory)

        assertEquals(
            FeedResponse(dogCategory, listOf(HUSKY_FAKE_ITEM_LIST)),
            dogsViewModel.user.value?.data
        )
    }

    // TODO: ERRO!Token é sempre nulo
    @Test
    fun `when call getDogsByCategory and get error response should update user property`() {
        val dogCategory = HUSKY
        val fakeErrorFeedResponse = FakeData.getFakeErrorFeedResponse(dogCategory)
        setResponseForRepositoryFeed(fakeErrorFeedResponse, dogCategory)
//        `when`(dogsViewModel.getToken()).thenReturn(FAKE_TOKEN)

        dogsViewModel.getDogsByCategory(dogCategory)

        assertEquals(ViewModelConstants.UNAUTHORIZED, dogsViewModel.user.value?.message)
    }



    // TODO: verificar se ao menos uma vez
    //  dogsViewModel.user.value recebe Resource.Loading<LoginResponse>
    //  quando login é chamado no ViewModel
    @Test
    fun `when call login should update user property once with loading state`() {
        val fakeErrorLoginResponse = FakeData.getFakeErrorLoginResponse()
        setResponseForRepositoryLogin(fakeErrorLoginResponse)
        dogsViewModel.user.observeForever(userLiveDataObserver)

        dogsViewModel.login()

        runBlocking {
            verify(userLiveDataObserver).onChanged(loading)
        }
    }

    // TODO: a instancia de SharedPref é sempre null
    @Test
    fun `when call login and emailToLogin isEquals storedEmail and storedToken isNotNullOrEmpty should not call login repository`() {
        dogsViewModel.emailToLogin = FAKE_EMAIL
        UserHelper.setStorage(sharedPref)
        val storage = UserHelper.getStorage()
        UserHelper.saveUser(FAKE_EMAIL, FAKE_TOKEN)
        `when`(storage?.getString(EMAIL_KEY, FAKE_EMAIL)).thenReturn(FAKE_EMAIL)
        `when`(storage?.getString(TOKEN_KEY, FAKE_TOKEN)).thenReturn(FAKE_TOKEN)
        val fakeSuccessLoginResponse = FakeData.getFakeSuccessLoginResponse()
        setResponseForRepositoryLogin(fakeSuccessLoginResponse)

        dogsViewModel.login()
        runBlocking {
            verify(mockDogsRepository, never()).login(emailToLogin)
        }
    }



    private fun setResponseForRepositoryLogin(fakeLoginResponse: Response<LoginResponse>) {
        runBlocking {
            `when`(mockDogsRepository.login(emailToLogin)).thenReturn(fakeLoginResponse)
        }
    }

    private fun setResponseForRepositoryFeed(
        fakeFeedResponse: Response<FeedResponse>,
        dogCategory: String
    ) {
        runBlocking {
            `when`(mockDogsRepository.getDogs(FAKE_TOKEN, dogCategory)).thenReturn(fakeFeedResponse)
        }
    }
}
