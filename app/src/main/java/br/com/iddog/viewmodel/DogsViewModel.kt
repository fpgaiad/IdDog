package br.com.iddog.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.data.store.Store
import br.com.iddog.repository.DogsRepository
import br.com.iddog.util.Resource
import br.com.iddog.util.ViewModelConstants
import kotlinx.coroutines.launch
import retrofit2.Response

class DogsViewModel @ViewModelInject constructor(
    private val repository: DogsRepository,
    private val userStore: Store
) : ViewModel() {

    private val _dogs: MutableLiveData<Resource<FeedResponse>> = MutableLiveData()
    val dogs: LiveData<Resource<FeedResponse>>
        get() = _dogs

    private val _user: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val user: LiveData<Resource<LoginResponse>>
        get() = _user

    fun login(email: String) {
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            val response = repository.login(email)
            response.body()?.user?.apply {
                userStore.saveUser(this.email, token)
            }
            _user.postValue(handleResponse(response))
        }
    }

    fun getDogsByCategory(category: String) {
        userStore.getUser()?.token?.let { token ->
            viewModelScope.launch {
                _dogs.postValue(Resource.Loading())
                val response = repository.getDogs(token, category)
                _dogs.postValue(handleResponse(response))
            }
        }
    }

    private inline fun <reified T> handleResponse(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            response.body()?.let { return Resource.Success(it) }
        }
        var errorMessage = ViewModelConstants.GENERIC_ERROR
        if (T::class == FeedResponse::class) errorMessage = ViewModelConstants.UNAUTHORIZED
        return Resource.Error(errorMessage)
    }
}