package com.thebridge.app.onboarding

import com.thebridge.app.data.local.UserMode
import com.thebridge.app.data.local.UserProfileEntity

/**
 * Pure logic for applying a mode change to an existing profile, kept
 * separate and unit-testable so a future refactor can't accidentally
 * clobber the covenant fields while changing the mode.
 */
object ModeSelectionUpdater {
    fun apply(profile: UserProfileEntity, newMode: UserMode): UserProfileEntity =
        profile.copy(selectedMode = newMode)
}
