package com.thebridge.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebridge.app.data.local.UserMode

private val Charcoal = Color(0xFF0A0500)
private val Gold = Color(0xFFD4AF37)
private val Parchment = Color(0xFFF5E8C7)
private val CardBackground = Color(0xFF1A120B)

private data class ModeCopy(val mode: UserMode, val title: String, val description: String)

private val MODE_COPY = listOf(
    ModeCopy(
        mode = UserMode.FOLLOWER,
        title = "Follower",
        description = "New here, or just starting to search? Begin gently, one step at a time.",
    ),
    ModeCopy(
        mode = UserMode.CAREGIVER,
        title = "Caregiver",
        description = "Caring for others, or missing a church community nearby? Find support that bridges the gap.",
    ),
    ModeCopy(
        mode = UserMode.ABIDE,
        title = "Abide",
        description = "Walked with God a while and know the language? Go deeper, direct and grounded.",
    ),
)

@Composable
fun ModeSelector(currentMode: UserMode? = null, onModeSelected: (UserMode) -> Unit) {
    var selected by remember { mutableStateOf(currentMode) }

    Surface(modifier = Modifier.fillMaxSize(), color = Charcoal) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Text(
                text = "Choose Your Path",
                color = Gold,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(24.dp))

            MODE_COPY.forEach { copy ->
                ModeCard(
                    title = copy.title,
                    description = copy.description,
                    isSelected = selected == copy.mode,
                    onClick = { selected = copy.mode },
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { selected?.let(onModeSelected) },
                enabled = selected != null,
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "Confirm", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ModeCard(title: String, description: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = CardBackground,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) Gold else Gold.copy(alpha = 0.3f)),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(text = title, color = Gold, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, color = Parchment, fontSize = 15.sp, lineHeight = 20.sp)
        }
    }
}
