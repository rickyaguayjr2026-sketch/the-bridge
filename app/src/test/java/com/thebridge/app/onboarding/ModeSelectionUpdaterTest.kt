package com.thebridge.app.onboarding

import com.thebridge.app.data.local.UserMode
import com.thebridge.app.data.local.UserProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class ModeSelectionUpdaterTest {

    @Test
    fun `applying a mode sets selectedMode without touching covenant fields`() {
        val profile = UserProfileEntity(
            selectedMode = null,
            covenantAcceptedAtEpochMillis = 12_345L,
            covenantCopyVersion = OnboardingRouter.CURRENT_COVENANT_VERSION,
            dataControlAcknowledged = true,
        )

        val updated = ModeSelectionUpdater.apply(profile, UserMode.CAREGIVER)

        assertEquals(UserMode.CAREGIVER, updated.selectedMode)
        assertEquals(profile.covenantAcceptedAtEpochMillis, updated.covenantAcceptedAtEpochMillis)
        assertEquals(profile.covenantCopyVersion, updated.covenantCopyVersion)
        assertEquals(profile.dataControlAcknowledged, updated.dataControlAcknowledged)
    }

    @Test
    fun `applying a mode overwrites a previously selected mode (Settings re-entry case)`() {
        val profile = UserProfileEntity(
            selectedMode = UserMode.FOLLOWER,
            covenantAcceptedAtEpochMillis = 1L,
            covenantCopyVersion = OnboardingRouter.CURRENT_COVENANT_VERSION,
            dataControlAcknowledged = true,
        )

        val updated = ModeSelectionUpdater.apply(profile, UserMode.ABIDE)

        assertEquals(UserMode.ABIDE, updated.selectedMode)
    }
}
