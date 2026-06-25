package com.example.thebridge.engine

import com.example.thebridge.modes.UserMode
import java.util.Calendar

enum class SpiritualState(val icon: String, val label: String) {
    REST("🕊️", "Rest"),
    PEACE("🛡️", "Peace"),
    FORGIVENESS("🩸", "Forgiveness"),
    JOY("🔥", "Joy"),
    NIGHT_PEACE("🌙", "Night Watch"),
    SILENT("🕯️", "Stillness")
}

data class WordForgeOutput(
    val state: SpiritualState,
    val counsel: String,
    val scripture: String,
    val scriptureRef: String,
    val recommendation: String
)

object WordForge {
    fun process(text: String, mode: UserMode = UserMode.FOLLOWER): WordForgeOutput {
        val input = text.lowercase().trim()
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        val state = when {
            input.containsAny("tired", "dragging", "weary") -> SpiritualState.REST
            input.containsAny("anxious", "scared", "afraid") -> SpiritualState.PEACE
            input.containsAny("guilt", "shame", "failed") -> SpiritualState.FORGIVENESS
            input.containsAny("thank", "grateful", "blessed") -> SpiritualState.JOY
            (currentHour >= 20 || currentHour <= 4) -> SpiritualState.NIGHT_PEACE
            else -> SpiritualState.SILENT
        }
        return buildOutput(state, mode)
    }

    private fun buildOutput(state: SpiritualState, mode: UserMode): WordForgeOutput {
        return when (mode) {
            UserMode.FOLLOWER -> buildFollowerOutput(state)
            UserMode.CAREGIVER -> buildCaregiverOutput(state)
            UserMode.ABIDE -> buildAbideOutput(state)
        }
    }

    private fun buildFollowerOutput(state: SpiritualState): WordForgeOutput = when (state) {
        SpiritualState.REST -> WordForgeOutput(state, "You are seen. Rest here.", "Come to me, all who are weary.", "Matthew 11:28", "Rest Room")
        SpiritualState.PEACE -> WordForgeOutput(state, "You are not alone in this.", "Do not be anxious about anything.", "Philippians 4:6", "Peace Room")
        SpiritualState.FORGIVENESS -> WordForgeOutput(state, "There is no condemnation here.", "There is now no condemnation.", "Romans 8:1", "Forgiveness Room")
        SpiritualState.JOY -> WordForgeOutput(state, "Let that gratitude go deep.", "Give thanks in all circumstances.", "1 Thessalonians 5:18", "Joy Room")
        SpiritualState.NIGHT_PEACE -> WordForgeOutput(state, "The night is His too.", "He grants sleep to those he loves.", "Psalm 127:2", "Night Watch")
        SpiritualState.SILENT -> WordForgeOutput(state, "Be still. He is here.", "Be still and know that I am God.", "Psalm 46:10", "Stillness Room")
    }

    private fun buildCaregiverOutput(state: SpiritualState): WordForgeOutput = when (state) {
        SpiritualState.REST -> WordForgeOutput(state, "You give so much. Receive now.", "Come to me, all who are weary.", "Matthew 11:28", "Rest Room")
        SpiritualState.PEACE -> WordForgeOutput(state, "Cast what you carry.", "Cast all your anxiety on him.", "1 Peter 5:7", "Peace Room")
        SpiritualState.FORGIVENESS -> WordForgeOutput(state, "You are covered. Keep going.", "There is now no condemnation.", "Romans 8:1", "Forgiveness Room")
        SpiritualState.JOY -> WordForgeOutput(state, "Your faithfulness is seen.", "Give thanks in all circumstances.", "1 Thessalonians 5:18", "Joy Room")
        SpiritualState.NIGHT_PEACE -> WordForgeOutput(state, "Leave it with Him tonight.", "He grants sleep to those he loves.", "Psalm 127:2", "Night Watch")
        SpiritualState.SILENT -> WordForgeOutput(state, "Stillness is not emptiness.", "Be still and know that I am God.", "Psalm 46:10", "Stillness Room")
    }

    private fun buildAbideOutput(state: SpiritualState): WordForgeOutput = when (state) {
        SpiritualState.REST -> WordForgeOutput(state, "Even the seasoned need the yoke.", "Come to me, all who are weary.", "Matthew 11:28", "Rest Room")
        SpiritualState.PEACE -> WordForgeOutput(state, "The peace that passes understanding is a weapon.", "The peace of God will guard your hearts.", "Philippians 4:7", "Peace Room")
        SpiritualState.FORGIVENESS -> WordForgeOutput(state, "Walk in it. Don't just know it.", "There is now no condemnation.", "Romans 8:1", "Forgiveness Room")
        SpiritualState.JOY -> WordForgeOutput(state, "Gratitude is warfare.", "Give thanks in all circumstances.", "1 Thessalonians 5:18", "Joy Room")
        SpiritualState.NIGHT_PEACE -> WordForgeOutput(state, "Watch and pray. The night is sacred.", "He grants sleep to those he loves.", "Psalm 127:2", "Night Watch")
        SpiritualState.SILENT -> WordForgeOutput(state, "You know what this silence is. Stay.", "Be still and know that I am God.", "Psalm 46:10", "Stillness Room")
    }

    private fun String.containsAny(vararg keywords: String) = keywords.any { this.contains(it) }
}
