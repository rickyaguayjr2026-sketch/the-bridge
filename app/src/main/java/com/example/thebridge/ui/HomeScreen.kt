package com.example.thebridge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thebridge.modes.UserMode

@Composable
fun HomeScreen(userMode: UserMode) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text("The Bridge", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(
                text = when (userMode) {
                    UserMode.FOLLOWER -> "Welcome. You are not alone."
                    UserMode.CAREGIVER -> "You give so much. Receive now."
                    UserMode.ABIDE -> "Go deeper. He is here."
                },
                color = Color.Gray, fontSize = 18.sp, textAlign = TextAlign.Center
            )
        }
    }
}
