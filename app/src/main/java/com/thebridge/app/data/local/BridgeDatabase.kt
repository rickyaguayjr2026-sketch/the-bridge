package com.thebridge.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        UserProfileEntity::class,
        WordContentEntity::class,
        WordProgressEntity::class,
        ConversationEntryEntity::class,
        WordForgeDecisionEntity::class,
        UserEventEntity::class,
    ],
    version = 1,
    exportSchema = true,
)
@TypeConverters(BridgeTypeConverters::class)
abstract class BridgeDatabase : RoomDatabase() {
    abstract fun onboardingDao(): OnboardingDao
    abstract fun wordDao(): WordDao
    abstract fun conversationDao(): ConversationDao
    abstract fun eventLogDao(): EventLogDao

    companion object {
        private const val DATABASE_NAME = "the-bridge.db"

        @Volatile
        private var instance: BridgeDatabase? = null

        fun getInstance(context: Context): BridgeDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context.applicationContext).also { instance = it }
            }

        private fun buildDatabase(context: Context): BridgeDatabase {
            val supportFactory = SupportFactory(
                DatabasePassphraseProvider(context).getOrCreatePassphrase(),
            )

            return Room.databaseBuilder(context, BridgeDatabase::class.java, DATABASE_NAME)
                .openHelperFactory(supportFactory)
                .build()
        }
    }
}
