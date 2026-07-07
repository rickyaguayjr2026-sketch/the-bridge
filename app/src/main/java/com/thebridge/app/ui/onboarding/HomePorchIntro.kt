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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

private val Charcoal = Color(0xFF0A0500)
private val Gold = Color(0xFFD4AF37)
private val Parchment = Color(0xFFF5E8C7)
private val CabinBody = Color(0xFF1A120B)

private val NARRATION_LINES = listOf(
    "You're here.",
    "This is the porch. Yours, now.",
    "In a moment, you'll choose how you want to walk through this — and who walks with you.",
    "Take your time. There's no wrong door.",
)

private const val LINE_DISPLAY_MS = 3_500L

/**
 * WordForge has no visual presence on this screen — narration only, voice
 * role (silent for now; no audio/TTS pipeline is wired up yet, and adding
 * one is a separate, not-yet-scoped decision).
 */
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

    Surface(modifier = Modifier.fillMaxSize(), color = Charcoal) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            // Placeholder geometric scene — stands in for real "Bible Project
            // style" illustrated artwork of a house/cabin exterior, porch
            // light lit, which still needs to be sourced/generated.
            PorchScene()

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
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "Continue", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PorchScene() {
    Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
        val bodyWidth = size.width * 0.55f
        val bodyHeight = size.height * 0.45f
        val bodyLeft = (size.width - bodyWidth) / 2f
        val bodyTop = size.height * 0.5f

        val roofOverhang = 14.dp.toPx()
        val roofPath = Path().apply {
            moveTo(bodyLeft - roofOverhang, bodyTop)
            lineTo(bodyLeft + bodyWidth / 2f, bodyTop - size.height * 0.22f)
            lineTo(bodyLeft + bodyWidth + roofOverhang, bodyTop)
            close()
        }
        drawPath(path = roofPath, color = Gold.copy(alpha = 0.85f))

        drawRect(
            color = CabinBody,
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyWidth, bodyHeight),
        )
        drawRect(
            color = Gold.copy(alpha = 0.6f),
            topLeft = Offset(bodyLeft, bodyTop),
            size = Size(bodyWidth, bodyHeight),
            style = Stroke(width = 2.dp.toPx()),
        )

        // Lit porch light beside the door — a warm point of light, not a
        // radiating glow (that read as WordForge's presence, which this
        // screen no longer has).
        drawCircle(
            color = Gold,
            radius = 5.dp.toPx(),
            center = Offset(bodyLeft + bodyWidth * 0.82f, bodyTop + bodyHeight * 0.55f),
        )
    }
}
