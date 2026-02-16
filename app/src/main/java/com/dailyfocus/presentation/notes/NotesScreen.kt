package com.dailyfocus.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dailyfocus.core.ui.theme.GradientFadeEnd
import com.dailyfocus.core.ui.theme.GradientFadeStart
import com.dailyfocus.core.ui.theme.Neutral90
import com.dailyfocus.core.ui.theme.Secondary40
import com.dailyfocus.presentation.notes.components.DailyFocusNoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: (Boolean) -> Unit, // Boolean: isChecklist
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState()
    var showSheet by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = Secondary40,
                contentColor = Color.White
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Note")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (notes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Rounded.Lightbulb, // Placeholder icon
                            contentDescription = null,
                            tint = Neutral90.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No notes yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Neutral90.copy(alpha = 0.7f)
                        )
                        Text(
                            text = "Capture thoughts and ideas",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Neutral90.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 16.dp,
                        bottom = 96.dp // FAB clearance
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Pinned Header logic could be here if we want headers, 
                    // but usually StaggeredGrid just mixes them. 
                    // Requirement: "Pinned notes first", sorted by query.
                    // So we just iterate.
                    items(notes, key = { it.id }) { note ->
                        DailyFocusNoteCard(
                            note = note,
                            onClick = { onNoteClick(note.id) },
                        )
                    }
                }
            }

            // Top Fade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GradientFadeEnd, GradientFadeStart) // Inverted for top? No.
                            // Fade from Opaque (Top) to Transparent (Bottom)?
                            // Usually "Top fade overlay" means covering content scrolling under header.
                            // But we have minimal header. Let's just do a simple gradient from Surface1 to Transparent
                        )
                    )
                    .align(Alignment.TopCenter)
            )

            // Bottom Fade
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(GradientFadeStart, GradientFadeEnd)
                        )
                    )
            )
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                containerColor = com.dailyfocus.core.ui.theme.Surface5
            ) {
                Column(
                    modifier = Modifier
                         .padding(bottom = 32.dp)
                         .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Create",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = Neutral90
                    )
                    
                    ListItem(
                        headlineContent = { Text("Text Note", color = Neutral90) },
                        leadingContent = { Icon(Icons.Rounded.Description, contentDescription = null, tint = Neutral90) },
                        modifier = Modifier.clickable { 
                            showSheet = false
                            onAddNoteClick(false) // isChecklist = false
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    
                    ListItem(
                        headlineContent = { Text("Checklist Note", color = Neutral90) },
                        leadingContent = { Icon(Icons.Rounded.CheckBox, contentDescription = null, tint = Neutral90) },
                        modifier = Modifier.clickable {
                            showSheet = false
                            onAddNoteClick(true) // isChecklist = true
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
    }
}
