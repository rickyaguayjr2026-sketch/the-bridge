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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.thebridge.app.data.local.UserProfileEntity
import com.thebridge.app.onboarding.AppScreen
import com.thebridge.app.onboarding.ModeSelectionUpdater
import com.thebridge.app.onboarding.OnboardingRouter
import com.thebridge.app.ui.onboarding.CovenantIntro
import com.thebridge.app.ui.onboarding.ModeSelector
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BridgeRoot() }
    }
}

private const val ROUTE_COVENANT = "covenant"
private const val ROUTE_MODE_SELECT = "mode_select"
private const val ROUTE_HOME = "home"

// AppScreen.LOADING is never returned by OnboardingRouter.resolve(); this branch
// exists only so the `when` stays exhaustive against the shared enum.
private fun routeFor(screen: AppScreen): String = when (screen) {
    AppScreen.LOADING -> ROUTE_COVENANT
    AppScreen.COVENANT -> ROUTE_COVENANT
    AppScreen.MODE_SELECT -> ROUTE_MODE_SELECT
    AppScreen.HOME -> ROUTE_HOME
}

@Composable
private fun BridgeRoot() {
    val app = LocalContext.current.applicationContext as BridgeApp
    val dao = remember { app.database.onboardingDao() }
    val scope = rememberCoroutineScope()

    var initialScreen by remember { mutableStateOf<AppScreen?>(null) }

    LaunchedEffect(Unit) {
        initialScreen = OnboardingRouter.resolve(dao.getProfile())
    }

    val resolved = initialScreen
    if (resolved == null) {
        LoadingScreen()
        return
    }

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = routeFor(resolved)) {
        composable(ROUTE_COVENANT) {
            CovenantIntro(
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
                        navController.navigate(ROUTE_MODE_SELECT) {
                            popUpTo(ROUTE_COVENANT) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(ROUTE_MODE_SELECT) {
            ModeSelector(
                currentMode = null,
                onModeSelected = { mode ->
                    scope.launch {
                        val current = dao.getProfile()
                        if (current != null) {
                            dao.upsertProfile(ModeSelectionUpdater.apply(current, mode))
                        }
                        navController.navigate(ROUTE_HOME) {
                            popUpTo(ROUTE_MODE_SELECT) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(ROUTE_HOME) {
            PlaceholderScreen("Home — Layer 3, coming next")
        }
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
