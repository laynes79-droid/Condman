package com.example.condocare.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.condocare.data.model.Communication

@Dao
interface CommunicationDao {

    @Query("SELECT * FROM communications ORDER BY timestamp DESC")
    suspend fun getAllCommunications(): List<Communication>

    @Query("SELECT * FROM communications WHERE id = :communicationId")
    suspend fun getCommunicationById(communicationId: Int): Communication?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunications(communications: List<Communication>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommunication(communication: Communication)

    @Query("DELETE FROM communications")
    suspend fun deleteAllCommunications()
}
