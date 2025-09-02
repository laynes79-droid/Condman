package com.example.condocare.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condocare.data.CommunicationRepository
import com.example.condocare.data.model.Communication
import com.example.condocare.data.remote.dto.CreateCommunicationRequest
import com.example.condocare.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateCommunicationViewModel @Inject constructor(
    private val repository: CommunicationRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _creationResult = MutableLiveData<Result<Communication>>()
    val creationResult: LiveData<Result<Communication>> = _creationResult

    fun createCommunication(title: String, message: String, isEmergency: Boolean) {
        viewModelScope.launch {
            val token = sessionManager.fetchAuthToken()
            if (token == null) {
                _creationResult.postValue(Result.failure(Exception("User not authenticated")))
                return@launch
            }

            val request = CreateCommunicationRequest(title, message, isEmergency)
            val response = repository.createCommunication(token, request)

            if (response.isSuccessful) {
                response.body()?.let {
                    _creationResult.postValue(Result.success(it))
                } ?: _creationResult.postValue(Result.failure(Exception("Empty response body")))
            } else {
                _creationResult.postValue(Result.failure(Exception("Failed to create communication: ${response.code()}")))
            }
        }
    }
}
