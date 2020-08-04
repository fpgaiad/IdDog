package br.com.iddog.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.data.network.NetworkConstants
import br.com.iddog.repository.DogsRepository
import br.com.iddog.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class DogsViewModel @ViewModelInject constructor(
    private val repository: DogsRepository
) : ViewModel() {

    private val _dogs: MutableLiveData<Resource<FeedResponse>> = MutableLiveData()
    val dogs: LiveData<Resource<FeedResponse>>
        get() = _dogs

    private val _user: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val user: LiveData<Resource<LoginResponse>>
        get() = _user

    var emailToLogin: String = "your@email.com"
    var userData: MutableMap<String, String> = mutableMapOf()

    fun login() {
        if (userData.containsKey(emailToLogin)) {
            _user.postValue(Resource.Success())
        }
        viewModelScope.launch {
            _user.postValue(Resource.Loading())
            val response = repository.login(emailToLogin)
            _user.postValue(handleResponse(response))
        }
    }

    fun getDogsByCategory(category: String) = viewModelScope.launch {
        _dogs.postValue(Resource.Loading())
        val response =
            repository.getDogs(userData[emailToLogin] ?: NetworkConstants.TOKEN, category)
        _dogs.postValue(handleResponse(response))
    }

    private inline fun <reified T> handleResponse(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            if (T::class == LoginResponse::class) saveUserData(response as Response<LoginResponse>)
            response.body()?.let { return Resource.Success(it) }
        }
        return Resource.Error(response.message())
    }

    private fun saveUserData(response: Response<LoginResponse>) {
        response.body()?.user?.apply {
            userData[email] = token
        }
    }
}