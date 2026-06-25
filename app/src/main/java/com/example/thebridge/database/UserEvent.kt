package com.example.thebridge.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_events")
data class UserEvent(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventType: String,
    val spiritualState: String,
    val userMode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val payload: String = ""
)
