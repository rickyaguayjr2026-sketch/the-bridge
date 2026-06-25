package com.example.thebridge.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import net.sqlcipher.database.SupportFactory

@Database(entities = [UserEvent::class], version = 1, exportSchema = false)
abstract class BridgeDatabase : RoomDatabase() {
    abstract fun userEventDao(): UserEventDao

    companion object {
        @Volatile
        private var INSTANCE: BridgeDatabase? = null

        fun getInstance(context: Context, passphrase: ByteArray): BridgeDatabase {
            return INSTANCE ?: synchronized(this) {
                val factory = SupportFactory(passphrase)
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BridgeDatabase::class.java,
                    "bridge_database"
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
