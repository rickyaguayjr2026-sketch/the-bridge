package com.thebridge.app.ui.sanctuary

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Shared palette — matches AvatarWalkthrough.kt. If these get promoted to a
// single shared theme file later, update both call sites together.
private val Charcoal = Color(0xFF0A0500)
private val Gold = Color(0xFFD4AF37)
private val Parchment = Color(0xFFF5E8C7)

/**
 * The six pieces of the Armor of God, Ephesians 6:14-17, in verse order.
 *
 * Verse text is NIV (2011, Biblica/Zondervan) — verbatim per piece, do not
 * paraphrase or edit wording. NIV is copyrighted; Ricky made this call
 * explicitly, aware of the licensing considerations (Biblica's general-use
 * quotation allowance covers volume here, but Biblica's stated policy also
 * requires a license for use "in connection with" AI/ML features, which
 * this app has elsewhere — Ricky accepted that risk knowingly, this is not
 * a resolved/cleared legal question). Per Biblica's own attribution
 * requirement, "NIV" must appear at the end of each quotation — see
 * `verseReference` below, and the required copyright notice must appear
 * somewhere in the app (Settings/credits screen — not built yet, flagging
 * for whenever that screen gets built):
 *
 *   Scripture quotations taken from The Holy Bible, New International
 *   Version®, NIV®. Copyright © 1973, 1978, 1984, 2011 by Biblica, Inc.
 *   Used by permission. All rights reserved worldwide.
 *
 * Art: every piece renders as a labeled placeholder block (see
 * PlaceholderArmorArt below) until real illustrations are sourced and wired
 * in. Do NOT treat any piece's art as final; this includes Belt of Truth,
 * even though a Google Flow placeholder was generated for it — that render
 * was never confirmed as final art, only accepted as a stand-in.
 */
enum class ArmorPiece(
    val displayName: String,
    val verseReference: String,
    val verseText: String,
    val affirmation: String,
) {
    BELT_OF_TRUTH(
        displayName = "Belt of Truth",
        verseReference = "Ephesians 6:14 (NIV)",
        verseText = "Stand firm then, with the belt of truth buckled around your waist",
        affirmation = "I stand in what is true.",
    ),
    BREASTPLATE_OF_RIGHTEOUSNESS(
        displayName = "Breastplate of Righteousness",
        verseReference = "Ephesians 6:14 (NIV)",
        verseText = "with the breastplate of righteousness in place",
        affirmation = "My heart is guarded and made right.",
    ),
    SHOES_OF_THE_GOSPEL_OF_PEACE(
        displayName = "Shoes of the Gospel of Peace",
        verseReference = "Ephesians 6:15 (NIV)",
        verseText = "and with your feet fitted with the readiness that comes from the gospel of peace",
        affirmation = "I walk ready, at peace.",
    ),
    SHIELD_OF_FAITH(
        displayName = "Shield of Faith",
        verseReference = "Ephesians 6:16 (NIV)",
        verseText = "In addition to all this, take up the shield of faith, with which you " +
            "can extinguish all the flaming arrows of the evil one",
        affirmation = "I trust what I cannot see.",
    ),
    HELMET_OF_SALVATION(
        displayName = "Helmet of Salvation",
        verseReference = "Ephesians 6:17 (NIV)",
        verseText = "Take the helmet of salvation",
        affirmation = "My mind is protected.",
    ),
    SWORD_OF_THE_SPIRIT(
        displayName = "Sword of the Spirit",
        verseReference = "Ephesians 6:17 (NIV)",
        verseText = "and the sword of the Spirit, which is the word of God",
        affirmation = "God's word is my strength.",
    ),
}

/**
 * Sanctuary — the Armor of God ceremony. One screen, six sequential internal
 * steps. Each step: placeholder art + verbatim verse + affirmation, then a
 * tap-to-confirm advance (no auto-advance, matching the app's existing
 * pattern everywhere else — post-John path selection, WordForge delivery).
 *
 * On confirming the sixth piece, the fully-armored closing state shows once,
 * then [onCeremonyComplete] fires exactly once. The caller (see wiring notes
 * below) is responsible for logging the ledger event — this composable holds
 * no database dependency itself, same separation OnboardingRouter uses to
 * stay unit-testable without instrumentation.
 *
 * HARD RULE — do not violate this in any future edit: no streak counter, no
 * completion count, no visible progress indicator, no badge. The signal this
 * ceremony produces is backend-only instrumentation data. Nothing about "how
 * many times you've done this" is ever surfaced to the user, anywhere.
 */
@Composable
fun Sanctuary(onCeremonyComplete: () -> Unit) {
    var stepIndex by remember { mutableIntStateOf(0) }
    val pieces = ArmorPiece.entries
    val isClosingState = stepIndex >= pieces.size

    Surface(modifier = Modifier.fillMaxSize(), color = Charcoal) {
        if (!isClosingState) {
            ArmorStep(
                piece = pieces[stepIndex],
                onConfirm = { stepIndex += 1 },
            )
        } else {
            ArmoredClosing(onContinue = onCeremonyComplete)
        }
    }
}

@Composable
private fun ArmorStep(piece: ArmorPiece, onConfirm: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        PlaceholderArmorArt(piece = piece)

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = piece.displayName,
            color = Gold,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "\u201c${piece.verseText}\u201d",
            color = Parchment,
            fontSize = 17.sp,
            fontStyle = FontStyle.Italic,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = piece.verseReference,
            color = Parchment.copy(alpha = 0.6f),
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = piece.affirmation,
            color = Gold,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onConfirm,
            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Put it on", fontWeight = FontWeight.Bold)
        }
    }
}

/**
 * Placeholder art block — labeled, not final. Swap this out for
 * `Image(painterResource(id = piece.artResId), ...)` once real illustrations
 * (or a composited full-figure asset) are sourced. Do not remove the
 * "placeholder" label until Ricky confirms final art is wired in.
 */
@Composable
private fun PlaceholderArmorArt(piece: ArmorPiece) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .background(Charcoal)
            .border(width = 2.dp, color = Gold.copy(alpha = 0.5f), shape = RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = piece.displayName,
                color = Gold.copy(alpha = 0.7f),
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
            )
            Text(
                text = "(placeholder art)",
                color = Gold.copy(alpha = 0.4f),
                fontSize = 12.sp,
                fontStyle = FontStyle.Italic,
            )
        }
    }
}

@Composable
private fun ArmoredClosing(onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Closing placeholder — the fully-armored composite is the same
        // production dependency named for the individual pieces: either six
        // assets composited onto one base figure, or a single seventh asset
        // showing all pieces together. Not sourced yet.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .background(Charcoal)
                .border(width = 2.dp, color = Gold, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "(fully armored — placeholder)",
                color = Gold.copy(alpha = 0.7f),
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                textAlign = TextAlign.Center,
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Fully armored.",
            color = Gold,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onContinue,
            colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Charcoal),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "Continue", fontWeight = FontWeight.Bold)
        }
    }
}
