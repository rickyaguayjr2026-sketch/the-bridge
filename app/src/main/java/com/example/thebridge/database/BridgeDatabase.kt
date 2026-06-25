package com.example.thebridge.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [UserEvent::class],
    version = 1,
    exportSchema = false
)
abstract class BridgeDatabase : RoomDatabase() {
    abstract fun userEventDao(): UserEventDao
}
