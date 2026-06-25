package com.thebridge.app.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey val id: Long = 1,
    val selectedMode: UserMode,
    val covenantAcceptedAtEpochMillis: Long,
    val covenantCopyVersion: Int,
    val dataControlAcknowledged: Boolean,
)

@Entity(tableName = "word_content")
data class WordContentEntity(
    @PrimaryKey val contentKey: String,
    val title: String,
    val body: String,
    val contentType: String,
    val progressionIndex: Int,
    val approvedAtEpochMillis: Long,
)

@Entity(
    tableName = "word_progress",
    foreignKeys = [
        ForeignKey(
            entity = UserProfileEntity::class,
            parentColumns = ["id"],
            childColumns = ["profileId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = WordContentEntity::class,
            parentColumns = ["contentKey"],
            childColumns = ["contentKey"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["profileId", "contentKey"], unique = true),
        Index(value = ["profileId"]),
        Index(value = ["contentKey"]),
    ],
)
data class WordProgressEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val profileId: Long = 1,
    val contentKey: String,
    val lastViewedAtEpochMillis: Long,
    val completedAtEpochMillis: Long? = null,
)

@Entity(tableName = "conversation_entry", indices = [Index(value = ["sessionId", "createdAtEpochMillis"])])
data class ConversationEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: String,
    val actor: ConversationActor,
    val message: String,
    val disclaimerAcceptedAtEpochMillis: Long,
    val createdAtEpochMillis: Long,
    val humanApproved: Boolean = false,
)

@Entity(
    tableName = "wordforge_decision",
    foreignKeys = [
        ForeignKey(
            entity = ConversationEntryEntity::class,
            parentColumns = ["id"],
            childColumns = ["conversationEntryId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index(value = ["conversationEntryId"])],
)
data class WordForgeDecisionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val conversationEntryId: Long,
    val eventWindowStartEpochMillis: Long,
    val eventWindowEndEpochMillis: Long,
    val allowed: Boolean,
    val ruleVersion: Int,
    val rationale: String,
)

@Entity(tableName = "user_event_log", indices = [Index(value = ["source", "createdAtEpochMillis"])])
data class UserEventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val source: EventSource,
    val eventType: String,
    val payload: String,
    val createdAtEpochMillis: Long,
)
