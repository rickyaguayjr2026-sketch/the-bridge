package com.example.thebridge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.thebridge.modes.UserMode
import com.example.thebridge.ui.onboarding.CovenantIntro
import com.example.thebridge.ui.onboarding.ModeSelector

enum class AppScreen {
    COVENANT,
    MODE_SELECT,
    HOME
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BridgeApp()
        }
    }
}

@Composable
fun BridgeApp() {
    var screen by remember { mutableStateOf(AppScreen.COVENANT) }
    var userMode by remember { mutableStateOf<UserMode?>(null) }

    when (screen) {
        AppScreen.COVENANT -> {
            CovenantIntro(
                onCovenantAccepted = {
                    screen = AppScreen.MODE_SELECT
                }
            )
        }
        AppScreen.MODE_SELECT -> {
            ModeSelector(
                onModeSelected = { mode ->
                    userMode = mode
                    screen = AppScreen.HOME
                }
            )
        }
        AppScreen.HOME -> {
            HomeScreen(userMode = userMode!!)
        }
    }
}
