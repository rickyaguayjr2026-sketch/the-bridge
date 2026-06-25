package com.thebridge.app.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import net.sqlcipher.database.SQLiteDatabase

class DatabasePassphraseProvider(context: Context) {
    private val appContext = context.applicationContext

    private val securePrefs by lazy {
        val masterKey = MasterKey.Builder(appContext)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            appContext,
            PREFERENCES_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
        )
    }

    fun getOrCreatePassphrase(): ByteArray {
        val existingPassphrase = securePrefs.getString(PASSPHRASE_KEY, null)
        val passphrase = existingPassphrase ?: createAndPersistPassphrase()
        return SQLiteDatabase.getBytes(passphrase.toCharArray())
    }

    private fun createAndPersistPassphrase(): String {
        val randomBytes = ByteArray(PASSPHRASE_SIZE_BYTES)
        SecureRandom().nextBytes(randomBytes)
        val passphrase = randomBytes.joinToString(separator = "") { "%02x".format(it.toInt() and 0xFF) }
        securePrefs.edit().putString(PASSPHRASE_KEY, passphrase).apply()
        return passphrase
    }

    private companion object {
        const val PREFERENCES_NAME = "bridge_secure_storage"
        const val PASSPHRASE_KEY = "room_db_passphrase"
        const val PASSPHRASE_SIZE_BYTES = 32
    }
}
