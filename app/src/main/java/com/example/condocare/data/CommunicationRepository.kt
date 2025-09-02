package com.example.condocare.data

import com.example.condocare.data.local.CommunicationDao
import com.example.condocare.data.model.Communication
import com.example.condocare.data.remote.RemoteDataSource
import com.example.condocare.data.remote.dto.AddComplementRequest
import com.example.condocare.data.remote.dto.CreateCommunicationRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunicationRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val communicationDao: CommunicationDao
) {

    suspend fun getCommunications(token: String): Result<List<Communication>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.getCommunications(token)
                if (response.isSuccessful) {
                    response.body()?.let {
                        communicationDao.deleteAllCommunications()
                        communicationDao.insertCommunications(it)
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("Network error: ${response.code()}"))
                }
            } catch (e: Exception) {
                // Network failed, try to fetch from local cache
                try {
                    val localData = communicationDao.getAllCommunications()
                    if (localData.isNotEmpty()) {
                        Result.success(localData)
                    } else {
                        Result.failure(e)
                    }
                } catch (dbException: Exception) {
                    Result.failure(dbException)
                }
            }
        }
    }

    suspend fun getCommunicationDetail(token: String, id: Int): Result<Communication> {
        // For detail view, always fetch from network to ensure fresh data
        return withContext(Dispatchers.IO) {
            try {
                val response = remoteDataSource.getCommunicationDetail(token, id)
                if (response.isSuccessful) {
                    response.body()?.let {
                        communicationDao.insertCommunication(it) // Update cache
                        Result.success(it)
                    } ?: Result.failure(Exception("Empty response body"))
                } else {
                    Result.failure(Exception("Network error: ${response.code()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createCommunication(token: String, request: CreateCommunicationRequest) =
        remoteDataSource.createCommunication(token, request)

    suspend fun closeCommunication(token: String, id: Int) =
        remoteDataSource.closeCommunication(token, id)

    suspend fun addComplement(token: String, id: Int, request: AddComplementRequest) =
        remoteDataSource.addComplement(token, id, request)
}
