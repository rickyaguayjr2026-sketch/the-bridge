package com.thebridge.app.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thebridge.app.R

private val Charcoal = Color(0xFF0A0500)
private val Gold = Color(0xFFD4AF37)
private val CardBackground = Color(0xFF1A120B)

/**
 * Identifies a Biblical Avatar. Not yet persisted anywhere — there is no
 * schema field for a chosen avatar on UserProfileEntity today, and this
 * contract didn't call for adding one, so selection here is in-memory only
 * until that's decided.
 */
enum class BiblicalAvatar {
    MARY, SAMARITAN_WOMAN, ESTHER, MARY_MAGDALENE, DAVID, PAUL, PETER, MOSES
}

private val AVATAR_NAMES = mapOf(
    BiblicalAvatar.MARY to "Mary",
    BiblicalAvatar.SAMARITAN_WOMAN to "Samaritan Woman",
    BiblicalAvatar.ESTHER to "Esther",
    BiblicalAvatar.MARY_MAGDALENE to "Mary Magdalene",
    BiblicalAvatar.DAVID to "David",
    BiblicalAvatar.PAUL to "Paul",
    BiblicalAvatar.PETER to "Peter",
    BiblicalAvatar.MOSES to "Moses",
)

// Only avatars with finished, background-consistent artwork get a portrait
// here — the rest fall back to a text-only card until their art is ready.
private val AVATAR_PORTRAITS = mapOf(
    BiblicalAvatar.MOSES to R.drawable.avatar_moses,
)

@Composable
fun AvatarSelection(currentAvatar: BiblicalAvatar? = null, onAvatarSelected: (BiblicalAvatar) -> Unit) {
    var selected by remember { mutableStateOf(currentAvatar) }

    Surface(modifier = Modifier.fillMaxSize(), color = Charcoal) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text(
                    text = "Who will walk with you in this season?",
                    color = Gold,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.height(24.dp))

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    items(BiblicalAvatar.entries) { avatar ->
                        AvatarCard(
                            name = AVATAR_NAMES.getValue(avatar),
                            portraitRes = AVATAR_PORTRAITS[avatar],
                            isSelected = selected == avatar,
                            onClick = { selected = avatar },
                        )
                    }
                }
            }

            Button(
                onClick = { selected?.let(onAvatarSelected) },
                enabled = selected != null,
                colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(24.dp),
            ) {
                Text(text = "Confirm", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun AvatarCard(name: String, portraitRes: Int?, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = CardBackground,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(width = if (isSelected) 2.dp else 1.dp, color = if (isSelected) Gold else Gold.copy(alpha = 0.3f)),
    ) {
        Column(modifier = Modifier.padding(12.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            if (portraitRes != null) {
                Image(
                    painter = painterResource(id = portraitRes),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp)),
                )
                Spacer(modifier = Modifier.height(8.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }
            Text(text = name, color = Gold, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
