package com.thebridge.app.ui.onboarding

import android.media.MediaPlayer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebridge.app.R
import kotlinx.coroutines.delay

private val Charcoal = Color(0xFF0A0500)
private val Gold = Color(0xFFD4AF37)

private enum class CinematicBeat { VERSE, PRAISE, CONSENT }

private val BEAT_TIMING_MS = mapOf(
    CinematicBeat.VERSE to 4_500L,
    CinematicBeat.PRAISE to 5_500L,
)

@Composable
fun CovenantIntro(onCovenantAccepted: () -> Unit) {
    val context = LocalContext.current
    var beat by remember { mutableStateOf(CinematicBeat.VERSE) }

    DisposableEffect(Unit) {
        val player = MediaPlayer.create(context, R.raw.covenant_swell)
        player?.start()
        onDispose { player?.release() }
    }

    LaunchedEffect(Unit) {
        delay(BEAT_TIMING_MS.getValue(CinematicBeat.VERSE))
        beat = CinematicBeat.PRAISE
        delay(BEAT_TIMING_MS.getValue(CinematicBeat.PRAISE))
        beat = CinematicBeat.CONSENT
    }

    Surface(modifier = Modifier.fillMaxSize(), color = Charcoal) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            AnimatedVisibility(visible = beat == CinematicBeat.VERSE, enter = fadeIn(), exit = fadeOut()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "In the beginning was the Word",
                        color = Gold,
                        fontSize = 22.sp,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "John 1:1", color = Gold, fontSize = 16.sp)
                }
            }

            AnimatedVisibility(visible = beat == CinematicBeat.PRAISE, enter = fadeIn(), exit = fadeOut()) {
                Text(
                    text = "HOLY HOLY HOLY\nYOU ARE LORD GOD ALMIGHTY\nWORTHY IS THE LAMB",
                    color = Gold,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 2.sp,
                )
            }

            AnimatedVisibility(visible = beat == CinematicBeat.CONSENT, enter = fadeIn(), exit = fadeOut()) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Welcome Home.",
                        color = Gold,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "This app learns you.\nIt lives on your device.\nYou control it.\nIt can be wiped at any time.",
                        color = Color(0xFFF5E8C7),
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp,
                    )
                    Spacer(modifier = Modifier.height(40.dp))
                    Button(
                        onClick = onCovenantAccepted,
                        colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Text(text = "I Understand. Begin.", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
