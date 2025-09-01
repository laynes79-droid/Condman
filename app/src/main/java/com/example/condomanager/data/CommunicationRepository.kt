package com.example.condomanager.data

import android.content.Context
import com.example.condomanager.db.AppDatabase
import com.example.condomanager.db.CommunicationDao
import com.example.condomanager.model.Communication
import com.example.condomanager.model.User
import com.example.condomanager.network.ApiClient
import com.example.condomanager.network.RemoteDataSource

class CommunicationRepository(
    private val communicationDao: CommunicationDao,
    private val remoteDataSource: RemoteDataSource
) {

    suspend fun getCommunications(): List<Communication> {
        val remoteData = remoteDataSource.getCommunications()
        if (remoteData.isNotEmpty()) {
            communicationDao.clearCommunications()
            communicationDao.insertAllCommunications(remoteData)
        }
        return communicationDao.getAllCommunications()
    }

    suspend fun createCommunication(communication: Communication) {
        remoteDataSource.createCommunication(communication)?.let {
            communicationDao.insertCommunication(it)
        }
    }

    suspend fun updateCommunication(communication: Communication) {
        remoteDataSource.updateCommunication(communication)?.let {
            communicationDao.updateCommunication(it)
        }
    }

    suspend fun findUserByCredentials(contact: String, password: String): User? {
        return remoteDataSource.login(contact, password)
    }

    suspend fun getUserById(id: Long): User? {
        var user = communicationDao.getUserById(id)
        if (user == null) {
            user = remoteDataSource.getUser(id)
            user?.let { communicationDao.insertUser(it) }
        }
        return user
    }

    suspend fun addUser(user: User) {
        remoteDataSource.registerUser(user)?.let {
            communicationDao.insertUser(it)
        }
    }

    companion object {
        private const val BASE_URL = "http://10.0.2.2:3000/api/"

        @Volatile
        private var INSTANCE: CommunicationRepository? = null

        fun getInstance(context: Context): CommunicationRepository {
            return INSTANCE ?: synchronized(this) {
                if (INSTANCE == null) {
                    val database = AppDatabase.getDatabase(context)
                    val apiClient = ApiClient(BASE_URL)
                    val remoteDataSource = RemoteDataSource(apiClient.apiService)
                    INSTANCE = CommunicationRepository(database.communicationDao(), remoteDataSource)
                }
                INSTANCE!!
            }
        }

        fun setTestInstance(repository: CommunicationRepository) {
            INSTANCE = repository
        }

        fun resetInstance() {
            INSTANCE = null
        }
    }
}
