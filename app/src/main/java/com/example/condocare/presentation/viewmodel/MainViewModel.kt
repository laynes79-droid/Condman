package com.example.condocare.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.condocare.data.CommunicationRepository
import com.example.condocare.data.model.Communication
import com.example.condocare.util.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: CommunicationRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _communications = MutableLiveData<List<Communication>>()
    val communications: LiveData<List<Communication>> = _communications

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadCommunications() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            val token = sessionManager.fetchAuthToken()
            if (token == null) {
                _error.postValue("User not authenticated")
                _isLoading.postValue(false)
                return@launch
            }

            val result = repository.getCommunications(token)
            result.onSuccess {
                _communications.postValue(it)
                _error.postValue(null)
            }.onFailure {
                _error.postValue(it.message)
            }
            _isLoading.postValue(false)
        }
    }

    fun getUserRole(): String? {
        return sessionManager.fetchUserRole()
    }
}
