package com.dailyfocus.core.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Year

@Composable
fun CopyrightFooter(modifier: Modifier = Modifier) {
    val currentYear = Year.now().value
    
    Text(
        text = "© $currentYear Uzzal Rahman. All rights reserved.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
        textAlign = TextAlign.Center,
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        modifier = modifier
            .padding(top = 24.dp, bottom = 16.dp)
    )
}
