package com.example.condocare.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condocare.data.model.User
import com.example.condocare.data.remote.RemoteDataSource
import com.example.condocare.data.remote.dto.RegisterRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterUserViewModel @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : ViewModel() {

    private val _registrationResult = MutableLiveData<Result<User>>()
    val registrationResult: LiveData<Result<User>> = _registrationResult

    fun registerUser(name: String, email: String, password: String, apartment: String, role: String) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(name, email, password, apartment, role)
                val response = remoteDataSource.register(request)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _registrationResult.postValue(Result.success(it))
                    } ?: _registrationResult.postValue(Result.failure(Exception("Empty response body")))
                } else {
                    _registrationResult.postValue(Result.failure(Exception("Registration failed with code: ${response.code()}")))
                }
            } catch (e: Exception) {
                _registrationResult.postValue(Result.failure(e))
            }
        }
    }
}
