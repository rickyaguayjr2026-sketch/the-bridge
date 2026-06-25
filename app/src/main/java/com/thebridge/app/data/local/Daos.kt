package com.thebridge.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface OnboardingDao {
    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfile(): UserProfileEntity?

    @Upsert
    suspend fun upsertProfile(profile: UserProfileEntity)
}

@Dao
interface WordDao {
    @Query("SELECT * FROM word_content ORDER BY progressionIndex ASC")
    suspend fun getAllWordContent(): List<WordContentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun replaceWordContent(content: List<WordContentEntity>)

    @Query("SELECT * FROM word_progress WHERE profileId = :profileId ORDER BY lastViewedAtEpochMillis DESC")
    suspend fun getWordProgress(profileId: Long = 1): List<WordProgressEntity>

    @Upsert
    suspend fun upsertWordProgress(progress: WordProgressEntity)
}

@Dao
interface ConversationDao {
    @Query("SELECT * FROM conversation_entry WHERE sessionId = :sessionId ORDER BY createdAtEpochMillis ASC")
    suspend fun getConversation(sessionId: String): List<ConversationEntryEntity>

    @Insert
    suspend fun insertEntry(entry: ConversationEntryEntity): Long

    @Query("SELECT * FROM wordforge_decision WHERE conversationEntryId = :conversationEntryId")
    suspend fun getDecisionForEntry(conversationEntryId: Long): WordForgeDecisionEntity?

    @Upsert
    suspend fun upsertDecision(decision: WordForgeDecisionEntity)
}

@Dao
interface EventLogDao {
    @Query("SELECT * FROM user_event_log ORDER BY createdAtEpochMillis DESC")
    suspend fun getRecentEvents(): List<UserEventEntity>

    @Insert
    suspend fun insertEvent(event: UserEventEntity): Long
}
