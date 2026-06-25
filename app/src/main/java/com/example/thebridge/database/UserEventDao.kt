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
}
