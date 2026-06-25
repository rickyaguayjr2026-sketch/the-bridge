package com.example.thebridge.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.thebridge.modes.UserMode

@Composable
fun ModeSelector(onModeSelected: (UserMode) -> Unit) {

    val modes = listOf(
        Triple(UserMode.FOLLOWER, "Follower", "New. Searching. Broken.\nStart here."),
        Triple(UserMode.CAREGIVER, "Caregiver", "Isolated. Giving to others.\nBridging the distance."),
        Triple(UserMode.ABIDE, "Abide", "Seasoned. Walking in faith.\nGo deeper.")
    )

    var selected by remember { mutableStateOf<UserMode?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Who are you right now?",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            modes.forEach { (mode, title, description) ->
                val isSelected = selected == mode
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) Color.White else Color.Gray,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { selected = mode }
                        .padding(20.dp)
                ) {
                    Column {
                        Text(
                            text = title,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (selected != null) {
                androidx.compose.material3.Button(
                    onClick = { selected?.let { onModeSelected(it) } },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "This is me. Enter The Bridge.",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}
