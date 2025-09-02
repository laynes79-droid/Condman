package com.example.condocare.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condocare.data.CommunicationRepository
import com.example.condocare.data.model.Communication
import com.example.condocare.data.remote.dto.AddComplementRequest
import com.example.condocare.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommunicationDetailViewModel @Inject constructor(
    private val repository: CommunicationRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _communication = MutableLiveData<Communication>()
    val communication: LiveData<Communication> = _communication

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _updateResult = MutableLiveData<Result<Communication>>()
    val updateResult: LiveData<Result<Communication>> = _updateResult

    fun loadCommunicationDetail(communicationId: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val token = sessionManager.fetchAuthToken() ?: return@launch
            val result = repository.getCommunicationDetail(token, communicationId)
            result.onSuccess {
                _communication.postValue(it)
            }.onFailure {
                _error.postValue(it.message)
            }
            _isLoading.postValue(false)
        }
    }

    fun addComplement(communicationId: Int, message: String) {
        viewModelScope.launch {
            val token = sessionManager.fetchAuthToken() ?: return@launch
            val request = AddComplementRequest(message)
            val response = repository.addComplement(token, communicationId, request)
            if (response.isSuccessful) {
                response.body()?.let { _updateResult.postValue(Result.success(it)) }
            } else {
                _updateResult.postValue(Result.failure(Exception("Failed to add complement")))
            }
        }
    }

    fun closeCommunication(communicationId: Int) {
        viewModelScope.launch {
            val token = sessionManager.fetchAuthToken() ?: return@launch
            val response = repository.closeCommunication(token, communicationId)
            if (response.isSuccessful) {
                response.body()?.let { _updateResult.postValue(Result.success(it)) }
            } else {
                _updateResult.postValue(Result.failure(Exception("Failed to close communication")))
            }
        }
    }

    fun getUserRole(): String? {
        return sessionManager.fetchUserRole()
    }
}
