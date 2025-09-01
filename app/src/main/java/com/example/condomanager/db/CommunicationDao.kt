package com.example.condomanager.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.condomanager.model.Communication
import com.example.condomanager.model.User

@Dao
interface CommunicationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunication(communication: Communication)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllCommunications(communications: List<Communication>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateCommunication(communication: Communication)

    @Query("SELECT * FROM communications ORDER BY date DESC")
    suspend fun getAllCommunications(): List<Communication>

    @Query("SELECT * FROM communications WHERE dbId = :id")
    suspend fun getCommunicationById(id: Long): Communication?

    @Query("SELECT * FROM users WHERE name = :name LIMIT 1")
    suspend fun getUserByName(name: String): User?

    @Query("SELECT * FROM users WHERE contact = :contact AND password = :password LIMIT 1")
    suspend fun findUserByCredentials(contact: String, password: String): User?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): User?

    @Query("DELETE FROM communications")
    suspend fun clearCommunications()

    @Query("DELETE FROM users")
    suspend fun clearUsers()
}
