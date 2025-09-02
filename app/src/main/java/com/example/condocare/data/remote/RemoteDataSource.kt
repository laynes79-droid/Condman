package com.example.condocare.data.remote

import com.example.condocare.data.model.Communication
import com.example.condocare.data.model.User
import com.example.condocare.data.remote.dto.*
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiService: ApiService) {

    suspend fun login(request: LoginRequest): Response<TokenResponse> {
        return apiService.login(request)
    }

    suspend fun register(request: RegisterRequest): Response<User> {
        return apiService.register(request)
    }

    suspend fun getCommunications(token: String): Response<List<Communication>> {
        return apiService.getCommunications("Token $token")
    }

    suspend fun getCommunicationDetail(token: String, id: Int): Response<Communication> {
        return apiService.getCommunicationDetail("Token $token", id)
    }

    suspend fun createCommunication(token: String, request: CreateCommunicationRequest): Response<Communication> {
        return apiService.createCommunication("Token $token", request)
    }

    suspend fun closeCommunication(token: String, id: Int): Response<Communication> {
        return apiService.closeCommunication("Token $token", id)
    }

    suspend fun addComplement(token: String, id: Int, request: AddComplementRequest): Response<Communication> {
        return apiService.addComplement("Token $token", id, request)
    }
}
