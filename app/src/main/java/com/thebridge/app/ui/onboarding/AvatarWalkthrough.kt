package com.thebridge.app.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Charcoal = Color(0xFF0A0500)
private val Gold = Color(0xFFD4AF37)
private val Parchment = Color(0xFFF5E8C7)

/**
 * A stop is a real nav destination, not narration-only. Sticky Notes and
 * Worship Center are stub screens (title/header only, clearly non-final —
 * same placeholder pattern used elsewhere in the app) that the walkthrough
 * actually navigates to; Closing has no stub header, just the sign-off.
 */
enum class WalkthroughStop { STICKY_NOTES, WORSHIP_CENTER, CLOSING }

private val STOP_TITLES: Map<WalkthroughStop, String?> = mapOf(
    WalkthroughStop.STICKY_NOTES to "Sticky Note Ministries",
    WalkthroughStop.WORSHIP_CENTER to "Worship Center",
    WalkthroughStop.CLOSING to null,
)

// Locked narration copy — do not alter. All 8 avatars x 3 stops.
private val NARRATION: Map<BiblicalAvatar, Map<WalkthroughStop, String>> = mapOf(
    BiblicalAvatar.DAVID to mapOf(
        WalkthroughStop.STICKY_NOTES to "Leave a note here — whatever's on your mind, whatever you need to say.",
        WalkthroughStop.WORSHIP_CENTER to "Come here when you need to be still. Even I needed that more than I let on.",
        WalkthroughStop.CLOSING to "There's more ahead. Let's go find it.",
    ),
    BiblicalAvatar.PAUL to mapOf(
        WalkthroughStop.STICKY_NOTES to "Write it down. Sometimes putting words to it is half the work.",
        WalkthroughStop.WORSHIP_CENTER to "This is a place to just be quiet with God for a while.",
        WalkthroughStop.CLOSING to "Still more to explore. Keep going.",
    ),
    BiblicalAvatar.PETER to mapOf(
        WalkthroughStop.STICKY_NOTES to "Say what's true, even if it's messy. This is a safe place for that.",
        WalkthroughStop.WORSHIP_CENTER to "Some things you don't need to figure out — just sit with them here.",
        WalkthroughStop.CLOSING to "There's more waiting. Come on.",
    ),
    BiblicalAvatar.MOSES to mapOf(
        WalkthroughStop.STICKY_NOTES to "Leave it here. You don't need the right words, just honest ones.",
        WalkthroughStop.WORSHIP_CENTER to "This is where you go to just listen for a while.",
        WalkthroughStop.CLOSING to "More is ahead when you're ready for it.",
    ),
    BiblicalAvatar.MARY to mapOf(
        WalkthroughStop.STICKY_NOTES to "Leave what you're carrying here.",
        WalkthroughStop.WORSHIP_CENTER to "A quiet place to just be, without needing to understand everything yet.",
        WalkthroughStop.CLOSING to "There's more ahead, even if you don't know it all yet.",
    ),
    BiblicalAvatar.RAHAB to mapOf(
        WalkthroughStop.STICKY_NOTES to "This is a safe place to leave what's heavy.",
        WalkthroughStop.WORSHIP_CENTER to "Come sit a while. You belong here.",
        WalkthroughStop.CLOSING to "There's more ahead for you too.",
    ),
    BiblicalAvatar.RUTH to mapOf(
        WalkthroughStop.STICKY_NOTES to "Leave what's on your heart here.",
        WalkthroughStop.WORSHIP_CENTER to "This is where you gather what you need.",
        WalkthroughStop.CLOSING to "I'll be here. Come back when you need me.",
    ),
    BiblicalAvatar.MARY_MAGDALENE to mapOf(
        WalkthroughStop.STICKY_NOTES to "Leave it here — whatever you need to say.",
        WalkthroughStop.WORSHIP_CENTER to "This is where you catch your breath.",
        WalkthroughStop.CLOSING to "There's more to see. Come find it.",
    ),
)

@Composable
fun AvatarWalkthroughStop(
    avatar: BiblicalAvatar,
    stop: WalkthroughStop,
    actionLabel: String,
    onAction: () -> Unit,
) {
    val portraitRes = AVATAR_PORTRAITS.getValue(avatar)
    val narration = NARRATION.getValue(avatar).getValue(stop)
    val stubTitle = STOP_TITLES[stop]

    Surface(modifier = Modifier.fillMaxSize(), color = Charcoal) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            if (stubTitle != null) {
                Text(text = stubTitle, color = Gold, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = "(Coming soon)", color = Gold.copy(alpha = 0.6f), fontSize = 13.sp)
                Spacer(modifier = Modifier.height(24.dp))
            }

            Image(
                painter = painterResource(id = portraitRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(160.dp)
                    .clip(RoundedCornerShape(16.dp)),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = narration,
                color = Parchment,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                lineHeight = 26.sp,
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = actionLabel, fontWeight = FontWeight.Bold)
            }
        }
    }
}
