package com.example.condomanager.network

import com.example.condomanager.model.Communication
import com.example.condomanager.model.User

class RemoteDataSource(private val apiService: ApiService) {

    suspend fun getCommunications(): List<Communication> {
        val response = apiService.getCommunications()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        }
        return emptyList()
    }

    suspend fun createCommunication(communication: Communication): Communication? {
        val response = apiService.createCommunication(communication)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun updateCommunication(communication: Communication): Communication? {
        val response = apiService.updateCommunication(communication.dbId, communication)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun registerUser(user: User): User? {
        val response = apiService.registerUser(user)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun login(contact: String, password: String): User? {
        val credentials = mapOf("contact" to contact, "password" to password)
        val response = apiService.login(credentials)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun getUser(userId: Long): User? {
        val response = apiService.getUser(userId)
        return if (response.isSuccessful) response.body() else null
    }
}
