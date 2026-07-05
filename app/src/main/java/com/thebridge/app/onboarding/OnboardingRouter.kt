package com.thebridge.app.onboarding

import com.thebridge.app.data.local.UserProfileEntity

enum class AppScreen { LOADING, COVENANT, MODE_SELECT, HOME }

/**
 * Pure routing logic, kept free of Android/Room framework calls so it's
 * unit-testable without instrumentation.
 */
object OnboardingRouter {
    const val CURRENT_COVENANT_VERSION = 1

    fun resolve(profile: UserProfileEntity?): AppScreen = when {
        profile == null -> AppScreen.COVENANT
        profile.covenantCopyVersion < CURRENT_COVENANT_VERSION -> AppScreen.COVENANT
        profile.selectedMode == null -> AppScreen.MODE_SELECT
        else -> AppScreen.HOME
    }
}
