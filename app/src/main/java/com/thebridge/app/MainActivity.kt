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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.thebridge.app.data.local.UserProfileEntity
import com.thebridge.app.onboarding.AppScreen
import com.thebridge.app.onboarding.ModeSelectionUpdater
import com.thebridge.app.onboarding.OnboardingRouter
import com.thebridge.app.ui.onboarding.AvatarSelection
import com.thebridge.app.ui.onboarding.AvatarWalkthroughStop
import com.thebridge.app.ui.onboarding.BiblicalAvatar
import com.thebridge.app.ui.onboarding.CovenantIntro
import com.thebridge.app.ui.onboarding.HomePorchIntro
import com.thebridge.app.ui.onboarding.ModeSelector
import com.thebridge.app.ui.onboarding.WalkthroughStop
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BridgeRoot() }
    }
}

private const val ROUTE_COVENANT = "covenant"
private const val ROUTE_HOME_PORCH_INTRO = "home_porch_intro"
private const val ROUTE_MODE_SELECT = "mode_select"
private const val ROUTE_AVATAR_SELECTION = "avatar_selection"
private const val ARG_AVATAR = "avatar"
private const val ROUTE_WALKTHROUGH_STICKY_NOTES = "walkthrough_sticky_notes/{$ARG_AVATAR}"
private const val ROUTE_WALKTHROUGH_WORSHIP_CENTER = "walkthrough_worship_center/{$ARG_AVATAR}"
private const val ROUTE_WALKTHROUGH_CLOSING = "walkthrough_closing/{$ARG_AVATAR}"
private const val ROUTE_HOME = "home"

private fun walkthroughStickyNotesRoute(avatar: BiblicalAvatar) = "walkthrough_sticky_notes/${avatar.name}"
private fun walkthroughWorshipCenterRoute(avatar: BiblicalAvatar) = "walkthrough_worship_center/${avatar.name}"
private fun walkthroughClosingRoute(avatar: BiblicalAvatar) = "walkthrough_closing/${avatar.name}"

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
                        navController.navigate(ROUTE_HOME_PORCH_INTRO) {
                            popUpTo(ROUTE_COVENANT) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(ROUTE_HOME_PORCH_INTRO) {
            HomePorchIntro(
                onContinue = {
                    navController.navigate(ROUTE_MODE_SELECT) {
                        popUpTo(ROUTE_HOME_PORCH_INTRO) { inclusive = true }
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
                        navController.navigate(ROUTE_AVATAR_SELECTION) {
                            popUpTo(ROUTE_MODE_SELECT) { inclusive = true }
                        }
                    }
                }
            )
        }

        composable(ROUTE_AVATAR_SELECTION) {
            AvatarSelection(
                currentAvatar = null,
                onAvatarSelected = { avatar ->
                    // Avatar choice is not yet persisted — no schema field exists for it
                    // yet and this contract didn't call for adding one. See PR notes.
                    navController.navigate(walkthroughStickyNotesRoute(avatar)) {
                        popUpTo(ROUTE_AVATAR_SELECTION) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = ROUTE_WALKTHROUGH_STICKY_NOTES,
            arguments = listOf(navArgument(ARG_AVATAR) { type = NavType.StringType }),
        ) { backStackEntry ->
            val avatar = BiblicalAvatar.valueOf(backStackEntry.arguments!!.getString(ARG_AVATAR)!!)
            AvatarWalkthroughStop(
                avatar = avatar,
                stop = WalkthroughStop.STICKY_NOTES,
                actionLabel = "Next",
                onAction = {
                    navController.navigate(walkthroughWorshipCenterRoute(avatar)) {
                        popUpTo(ROUTE_WALKTHROUGH_STICKY_NOTES) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = ROUTE_WALKTHROUGH_WORSHIP_CENTER,
            arguments = listOf(navArgument(ARG_AVATAR) { type = NavType.StringType }),
        ) { backStackEntry ->
            val avatar = BiblicalAvatar.valueOf(backStackEntry.arguments!!.getString(ARG_AVATAR)!!)
            AvatarWalkthroughStop(
                avatar = avatar,
                stop = WalkthroughStop.WORSHIP_CENTER,
                actionLabel = "Next",
                onAction = {
                    navController.navigate(walkthroughClosingRoute(avatar)) {
                        popUpTo(ROUTE_WALKTHROUGH_WORSHIP_CENTER) { inclusive = true }
                    }
                }
            )
        }

        composable(
            route = ROUTE_WALKTHROUGH_CLOSING,
            arguments = listOf(navArgument(ARG_AVATAR) { type = NavType.StringType }),
        ) { backStackEntry ->
            val avatar = BiblicalAvatar.valueOf(backStackEntry.arguments!!.getString(ARG_AVATAR)!!)
            AvatarWalkthroughStop(
                avatar = avatar,
                stop = WalkthroughStop.CLOSING,
                actionLabel = "Enter",
                onAction = {
                    navController.navigate(ROUTE_HOME) {
                        popUpTo(ROUTE_WALKTHROUGH_CLOSING) { inclusive = true }
                    }
                }
            )
        }

        composable(ROUTE_HOME) {
            PlaceholderScreen("Home — porch-to-house transition, coming next")
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
