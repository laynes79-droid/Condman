package com.example.condocare.data.remote

import com.example.condocare.data.model.Communication
import com.example.condocare.data.model.User
import com.example.condocare.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("token/")
    suspend fun login(@Body request: LoginRequest): Response<TokenResponse>

    @POST("users/")
    suspend fun register(@Body request: RegisterRequest): Response<User>

    @GET("communications/")
    suspend fun getCommunications(@Header("Authorization") token: String): Response<List<Communication>>

    @GET("communications/{id}/")
    suspend fun getCommunicationDetail(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Communication>

    @POST("communications/")
    suspend fun createCommunication(
        @Header("Authorization") token: String,
        @Body request: CreateCommunicationRequest
    ): Response<Communication>

    @POST("communications/{id}/close/")
    suspend fun closeCommunication(
        @Header("Authorization") token: String,
        @Path("id") id: Int
    ): Response<Communication>

    @POST("communications/{id}/add_complement/")
    suspend fun addComplement(
        @Header("Authorization") token: String,
        @Path("id") id: Int,
        @Body request: AddComplementRequest
    ): Response<Communication>
}
