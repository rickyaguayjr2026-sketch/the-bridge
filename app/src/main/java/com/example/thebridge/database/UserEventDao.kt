package com.example.thebridge.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserEventDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertEvent(event: UserEvent)

    @Query("SELECT * FROM user_events ORDER BY timestamp DESC")
    fun getAllEvents(): Flow<List<UserEvent>>

    @Query("SELECT * FROM user_events WHERE userMode = :mode ORDER BY timestamp DESC")
    fun getEventsByMode(mode: String): Flow<List<UserEvent>>

    @Query("SELECT * FROM user_events WHERE spiritualState = :state ORDER BY timestamp DESC")
    fun getEventsByState(state: String): Flow<List<UserEvent>>

    @Query("DELETE FROM user_events")
    suspend fun wipeAllEvents()
}
