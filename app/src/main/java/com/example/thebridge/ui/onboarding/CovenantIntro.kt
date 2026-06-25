package com.example.thebridge.ui.onboarding

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer

@Composable
fun CovenantIntro(onCovenantAccepted: () -> Unit) {
    val context = LocalContext.current
    var visible by remember { mutableStateOf(false) }

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/raw/covenant_intro")
            setMediaItem(mediaItem)
            prepare()
            seekTo(74_000L)
            playWhenReady = true
        }
    }

    LaunchedEffect(Unit) { visible = true }

    DisposableEffect(Unit) {
        onDispose {
            player.stop()
            player.release()
        }
    }

    LaunchedEffect(player) {
        player.addListener(object : Player.Listener {
            override fun onPositionDiscontinuity(
                oldPosition: Player.PositionInfo,
                newPosition: Player.PositionInfo,
                reason: Int
            ) {
                if (player.currentPosition >= 94_000L) player.stop()
            }
        })
    }

    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 2000)
    )

    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black).alpha(alpha),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(32.dp)
        ) {
            Text("The Bridge", color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(
                text = "This app learns you.\nIt lives on your device.\nYou control it.\nIt can be wiped at any time.",
                color = Color.White, fontSize = 16.sp, textAlign = TextAlign.Center, lineHeight = 28.sp
            )
            Text("Nothing leaves this device without your knowledge.", color = Color.Gray, fontSize = 14.sp, textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { player.stop(); onCovenantAccepted() }, modifier = Modifier.fillMaxWidth()) {
                Text("I understand. Enter.", fontSize = 16.sp)
            }
        }
    }
}
