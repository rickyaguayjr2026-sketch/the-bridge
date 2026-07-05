package com.thebridge.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.thebridge.app.data.local.UserProfileEntity
import com.thebridge.app.onboarding.AppScreen
import com.thebridge.app.onboarding.OnboardingRouter
import com.thebridge.app.ui.onboarding.CovenantIntro
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BridgeRoot() }
    }
}

@Composable
private fun BridgeRoot() {
    val app = LocalContext.current.applicationContext as BridgeApp
    val dao = remember { app.database.onboardingDao() }
    val scope = rememberCoroutineScope()

    var screen by remember { mutableStateOf(AppScreen.LOADING) }

    LaunchedEffect(Unit) {
        screen = OnboardingRouter.resolve(dao.getProfile())
    }

    when (screen) {
        AppScreen.LOADING -> LoadingScreen()

        AppScreen.COVENANT -> CovenantIntro(
            onCovenantAccepted = {
                scope.launch {
                    dao.upsertProfile(
                        UserProfileEntity(
                            selectedMode = null,
                            covenantAcceptedAtEpochMillis = System.currentTimeMillis(),
                            covenantCopyVersion = OnboardingRouter.CURRENT_COVENANT_VERSION,
                            dataControlAcknowledged = true,
                        )
                    )
                    screen = AppScreen.MODE_SELECT
                }
            }
        )

        AppScreen.MODE_SELECT -> PlaceholderScreen("Mode Selector — Layer 2, coming next")

        AppScreen.HOME -> PlaceholderScreen("Home — Layer 3, coming next")
    }
}

@Composable
private fun LoadingScreen() {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {}
}

@Composable
private fun PlaceholderScreen(message: String) {
    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = "The Bridge", style = MaterialTheme.typography.headlineMedium)
            Text(text = message, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
