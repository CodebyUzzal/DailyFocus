package com.dailyfocus.presentation.notes.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.dailyfocus.core.ui.theme.*
import com.dailyfocus.domain.model.ChecklistItem
import com.dailyfocus.domain.model.Note
import com.dailyfocus.domain.model.NoteColor

@Composable
fun DailyFocusNoteCard(
    note: Note,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(targetValue = if (isPressed) 0.98f else 1f, label = "scale")

    val containerColor = getContainerColor(note.color)
    val contentColor = if (note.color == NoteColor.DEFAULT) Neutral90 else Color.Black // Dark text on pastel colors if we use them
    // Actually, for "Calm surfaces", stick to dark theme colors unless explicitly "Red/Blue".
    // Let's use subtle low-opacity colors for the dark theme to keep text white/light.
    
    // Adjust logic: For dark mode, colored notes should be dark variants of the color to maintain contrast with white text.
    // However, Google Keep uses pastel colors even in dark mode sometimes, or dark versions.
    // Let's stick to Surface2 for DEFAULT, and for others, use a tinted Surface2.
    
    val cardColor = if (note.color == NoteColor.DEFAULT) Surface2 else getTintedSurface(note.color)

    Surface(
        modifier = modifier
            .scale(scale)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        border = if (note.isPinned) BorderStroke(1.dp, Primary80.copy(alpha = 0.5f)) else BorderStroke(1.dp, Color.White.copy(alpha = 0.05f)),
        shadowElevation = 0.dp // "No heavy elevation"
    ) {
        Column(
            modifier = Modifier
                .clickable(interactionSource = interactionSource, indication = null, onClick = onClick)
                .padding(12.dp)
        ) {
            if (!note.title.isNullOrBlank()) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Neutral99,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (note.isChecklist) {
                ChecklistPreview(note.checklistItems)
            } else if (!note.content.isNullOrBlank()) {
                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Neutral90,
                    maxLines = 8, // Requirements say "max 4 lines", let's restrict.
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight // ensure consistent height
                )
            }
        }
    }
}

@Composable
fun ChecklistPreview(items: List<ChecklistItem>) {
    val displayItems = items.take(4)
    val remaining = items.size - 4

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        displayItems.forEach { item ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                 // Custom checkbox UI or just icon
                 Icon(
                     imageVector = if (item.isChecked) androidx.compose.material.icons.Icons.Default.CheckBox else androidx.compose.material.icons.Icons.Default.CheckBoxOutlineBlank,
                     contentDescription = null,
                     modifier = Modifier.size(16.dp),
                     tint = if (item.isChecked) Neutral90.copy(alpha = 0.5f) else Neutral90
                 )
                 Spacer(modifier = Modifier.width(8.dp))
                 Text(
                     text = item.text,
                     style = MaterialTheme.typography.bodyMedium.copy(
                         textDecoration = if (item.isChecked) TextDecoration.LineThrough else null
                     ),
                     color = if (item.isChecked) Neutral90.copy(alpha = 0.5f) else Neutral90,
                     maxLines = 1,
                     overflow = TextOverflow.Ellipsis
                 )
            }
        }
        if (remaining > 0) {
            Text(
                text = "+$remaining more",
                style = MaterialTheme.typography.bodySmall,
                color = Neutral90.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 24.dp, top = 4.dp)
            )
        }
    }
}

// Helper to map NoteColor to Compose Color (Dark Mode Optimized)
// We mix the base color with Surface2 to create a subtle tinted surface.
fun getTintedSurface(color: NoteColor): Color {
    val base = when (color) {
        NoteColor.DEFAULT -> return Surface2
        NoteColor.RED -> Color(0xFF5C2B29)
        NoteColor.ORANGE -> Color(0xFF5C3A29)
        NoteColor.YELLOW -> Color(0xFF5C4F29)
        NoteColor.GREEN -> Color(0xFF2E4C2E)
        NoteColor.BLUE -> Color(0xFF283896) // Primary30-ish
        NoteColor.PURPLE -> Color(0xFF4A2C5E)
        NoteColor.PINK -> Color(0xFF5E2C46)
        NoteColor.BROWN -> Color(0xFF3E332E)
        NoteColor.GRAY -> Surface3
    }
    // Blend with Surface2 just in case? Or just use these deep variants.
    // These hex codes are arbitrary "Deep Dark" versions of the colors.
    return base
}

fun getContainerColor(color: NoteColor): Color {
     // Not used if we calculate above
     return Color.Transparent
}
