package com.thebridge.app

import android.app.Application
import com.thebridge.app.data.local.BridgeDatabase

class BridgeApp : Application() {
    val database: BridgeDatabase by lazy { BridgeDatabase.getInstance(this) }
}
