package com.thebridge.app.ui.onboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val DuskBackground = Color(0xFF3D362E)
private val PorchGlow = Color(0xFFE8C79A)
private val Parchment = Color(0xFFF5E8C7)

private val NARRATION_LINES = listOf(
    "You're here.",
    "This is the porch. Yours, now.",
    "In a moment, you'll choose how you want to walk through this — and who walks with you.",
    "Take your time. There's no wrong door.",
)

private const val LINE_DISPLAY_MS = 3_500L

@Composable
fun HomePorchIntro(onContinue: () -> Unit) {
    var lineIndex by remember { mutableIntStateOf(0) }
    var narrationDone by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        for (index in NARRATION_LINES.indices) {
            lineIndex = index
            delay(LINE_DISPLAY_MS)
        }
        narrationDone = true
    }

    Surface(modifier = Modifier.fillMaxSize(), color = DuskBackground) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            PorchLightGlow()

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(visible = !narrationDone, enter = fadeIn(), exit = fadeOut()) {
                Text(
                    text = NARRATION_LINES[lineIndex.coerceIn(0, NARRATION_LINES.lastIndex)],
                    color = Parchment,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 28.sp,
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            AnimatedVisibility(visible = narrationDone, enter = fadeIn()) {
                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(containerColor = PorchGlow, contentColor = DuskBackground),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Continue", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PorchLightGlow() {
    Canvas(modifier = Modifier.size(140.dp)) {
        val glowBrush = Brush.radialGradient(
            colors = listOf(PorchGlow.copy(alpha = 0.9f), PorchGlow.copy(alpha = 0.15f), Color.Transparent),
        )
        drawCircle(brush = glowBrush)
    }
}
