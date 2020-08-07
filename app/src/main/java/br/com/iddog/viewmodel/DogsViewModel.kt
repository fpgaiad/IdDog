package br.com.iddog.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.iddog.data.model.feed.FeedResponse
import br.com.iddog.data.model.login.LoginResponse
import br.com.iddog.repository.DogsRepository
import br.com.iddog.util.Resource
import br.com.iddog.util.UserHelper
import br.com.iddog.util.UserHelper.EMAIL_KEY
import br.com.iddog.util.UserHelper.INVALID_EMAIL
import br.com.iddog.util.UserHelper.TOKEN_KEY
import br.com.iddog.util.ViewModelConstants
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

    var emailToLogin: String = ""

    fun login() {
        val storage = UserHelper.getStorage()
        if (emailToLogin == storage?.getString(EMAIL_KEY, INVALID_EMAIL)
            && !storage.getString(TOKEN_KEY, null).isNullOrEmpty()
        ) {
            _user.postValue(Resource.Success())
            return
        } else {
            viewModelScope.launch {
                _user.postValue(Resource.Loading())
                val response = repository.login(emailToLogin)
                _user.postValue(handleResponse(response))
            }
        }
    }

    fun getDogsByCategory(category: String) {
        val token = getToken()
        token?.let {
            viewModelScope.launch {
                _dogs.postValue(Resource.Loading())
                val response = repository.getDogs(token, category)
                _dogs.postValue(handleResponse(response))
            }
        }
    }

    private fun getToken(): String? {
        val storage = UserHelper.getStorage()
        return when {
            !storage?.getString(TOKEN_KEY, null).isNullOrEmpty() -> {
                storage?.getString(TOKEN_KEY, null).toString()
            }
            else -> {
                _dogs.postValue(Resource.Error(ViewModelConstants.INVALID_TOKEN))
                null
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