package com.thebridge.app.onboarding

import com.thebridge.app.data.local.UserMode
import com.thebridge.app.data.local.UserProfileEntity
import org.junit.Assert.assertEquals
import org.junit.Test

class OnboardingRouterTest {

    @Test
    fun `fresh install with no profile shows Covenant Intro`() {
        assertEquals(AppScreen.COVENANT, OnboardingRouter.resolve(null))
    }

    @Test
    fun `app killed after covenant accepted but before mode chosen resumes at Mode Selector`() {
        val profile = UserProfileEntity(
            selectedMode = null,
            covenantAcceptedAtEpochMillis = 1_000L,
            covenantCopyVersion = OnboardingRouter.CURRENT_COVENANT_VERSION,
            dataControlAcknowledged = true,
        )
        assertEquals(AppScreen.MODE_SELECT, OnboardingRouter.resolve(profile))
    }

    @Test
    fun `fully onboarded user goes straight to Home`() {
        val profile = UserProfileEntity(
            selectedMode = UserMode.FOLLOWER,
            covenantAcceptedAtEpochMillis = 1_000L,
            covenantCopyVersion = OnboardingRouter.CURRENT_COVENANT_VERSION,
            dataControlAcknowledged = true,
        )
        assertEquals(AppScreen.HOME, OnboardingRouter.resolve(profile))
    }

    @Test
    fun `consent copy version bump forces re-consent even with mode already chosen`() {
        val profile = UserProfileEntity(
            selectedMode = UserMode.ABIDE,
            covenantAcceptedAtEpochMillis = 1_000L,
            covenantCopyVersion = OnboardingRouter.CURRENT_COVENANT_VERSION - 1,
            dataControlAcknowledged = true,
        )
        assertEquals(AppScreen.COVENANT, OnboardingRouter.resolve(profile))
    }
}
