package com.example.condomanager.network

import com.example.condomanager.model.Communication
import com.example.condomanager.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Defines the REST API endpoints for the CondoManager application.
 * This interface is used by Retrofit to generate the network client.
 */
interface ApiService {

    /**
     * Registers a new user.
     * @param user The user object to register.
     * @return The newly created user object from the server.
     */
    @POST("register/")
    suspend fun registerUser(@Body user: User): Response<User>

    /**
     * Attempts to log in a user with the given credentials.
     * @param credentials A map containing the user's contact and password.
     * @return The user object if login is successful.
     */
    @POST("login/")
    suspend fun login(@Body credentials: Map<String, String>): Response<User>

    /**
     * Retrieves a specific user by their ID.
     * @param userId The ID of the user to retrieve.
     * @return The user object.
     */
    @GET("users/{id}/")
    suspend fun getUser(@Path("id") userId: Long): Response<User>

    /**
     * Retrieves the list of all communications.
     * @return A list of [Communication] objects.
     */
    @GET("communications/")
    suspend fun getCommunications(): Response<List<Communication>>

    /**
     * Creates a new communication.
     * @param communication The communication object to create.
     * @return The newly created communication object from the server.
     */
    @POST("communications/")
    suspend fun createCommunication(@Body communication: Communication): Response<Communication>

    /**
     * Updates an existing communication.
     * Used for adding complements or closing a communication.
     * @param communicationId The ID of the communication to update.
     * @param communication The updated communication object.
     * @return The updated communication object from the server.
     */
    @PUT("communications/{id}/")
    suspend fun updateCommunication(@Path("id") communicationId: Long, @Body communication: Communication): Response<Communication>
}
