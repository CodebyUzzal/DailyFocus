package com.dailyfocus.presentation.notes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dailyfocus.core.ui.theme.*
import com.dailyfocus.presentation.notes.components.DailyFocusNoteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    onNoteClick: (Long) -> Unit,
    onAddNoteClick: (Boolean) -> Unit, 
    viewModel: NotesViewModel = hiltViewModel()
) {
    val notes by viewModel.notes.collectAsState()
    var showSheet by remember { mutableStateOf(false) }
    var showDeleteAllDialog by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // Filter notes based on search query
    val filteredNotes = remember(notes, searchQuery) {
        if (searchQuery.isBlank()) notes
        else notes.filter { 
            (it.title?.contains(searchQuery, ignoreCase = true) == true) || 
            (it.items.any { item -> item.text.contains(searchQuery, ignoreCase = true) })
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showSheet = true },
                containerColor = Secondary40,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Note")
            }
        },
        containerColor = Surface1
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Bar / Search Bar Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Search Bar Lookalike
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = Surface2,
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Rounded.Menu, contentDescription = "Menu", tint = Neutral90)
                        Spacer(modifier = Modifier.width(16.dp))
                        
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Neutral90),
                            cursorBrush = androidx.compose.ui.graphics.SolidColor(Secondary40),
                            decorationBox = { innerTextField ->
                                if (searchQuery.isEmpty()) {
                                    Text("Search your notes", style = MaterialTheme.typography.bodyLarge, color = Neutral90.copy(alpha = 0.5f))
                                }
                                innerTextField()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        
                        if (searchQuery.isNotEmpty()) {
                             IconButton(onClick = { searchQuery = "" }) {
                                 Icon(Icons.Rounded.Close, contentDescription = "Clear", tint = Neutral90)
                             }
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Delete All Action
                        IconButton(onClick = { showDeleteAllDialog = true }) {
                             Icon(
                                 imageVector = Icons.Rounded.DeleteSweep, // Or Delete
                                 contentDescription = "Delete All",
                                 tint = Error40
                             )
                        }
                    }
                }
            }

            if (filteredNotes.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = if (searchQuery.isNotEmpty()) Icons.Default.Search else Icons.Rounded.Lightbulb,
                            contentDescription = null,
                            tint = Neutral90.copy(alpha = 0.3f),
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = if (searchQuery.isNotEmpty()) "No matches found" else "No notes yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = Neutral90.copy(alpha = 0.7f)
                        )
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Capture thoughts and ideas",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Neutral90.copy(alpha = 0.5f)
                            )
                        }
                    }
                }
            } else {
                LazyVerticalStaggeredGrid(
                    columns = StaggeredGridCells.Adaptive(160.dp),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 96.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalItemSpacing = 16.dp,
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredNotes, key = { it.id }) { note ->
                        DailyFocusNoteCard(
                            note = note,
                            onClick = { onNoteClick(note.id) },
                        )
                    }
                }
            }
        }
        
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                containerColor = Surface5
            ) {
                Column(
                    modifier = Modifier
                         .padding(bottom = 32.dp, start = 16.dp, end = 16.dp)
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
                            onAddNoteClick(false)
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                    
                    ListItem(
                        headlineContent = { Text("Checklist Note", color = Neutral90) },
                        leadingContent = { Icon(Icons.Rounded.CheckBox, contentDescription = null, tint = Neutral90) },
                        modifier = Modifier.clickable {
                            showSheet = false
                            onAddNoteClick(true)
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                    )
                }
            }
        }
        
        if (showDeleteAllDialog) {
            AlertDialog(
                onDismissRequest = { showDeleteAllDialog = false },
                title = { Text("Delete All Notes?") },
                text = { Text("This action cannot be undone. All your notes will be permanently removed.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteAllNotes()
                            showDeleteAllDialog = false
                        }
                    ) {
                        Text("Delete All", color = Error40)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteAllDialog = false }) {
                        Text("Cancel")
                    }
                },
                containerColor = Surface3,
                titleContentColor = Neutral90,
                textContentColor = Neutral90
            )
        }
    }
}

// Needed for BasicTextField
@Composable
fun BasicTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    textStyle: androidx.compose.ui.text.TextStyle = androidx.compose.ui.text.TextStyle.Default,
    keyboardOptions: androidx.compose.foundation.text.KeyboardOptions = androidx.compose.foundation.text.KeyboardOptions.Default,
    keyboardActions: androidx.compose.foundation.text.KeyboardActions = androidx.compose.foundation.text.KeyboardActions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    minLines: Int = 1,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    onTextLayout: (androidx.compose.ui.text.TextLayoutResult) -> Unit = {},
    interactionSource: androidx.compose.foundation.interaction.MutableInteractionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
    cursorBrush: androidx.compose.ui.graphics.Brush = androidx.compose.ui.graphics.SolidColor(Color.Black),
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit = @Composable { innerTextField -> innerTextField() }
) {
    androidx.compose.foundation.text.BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        readOnly = readOnly,
        textStyle = textStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        interactionSource = interactionSource,
        cursorBrush = cursorBrush,
        decorationBox = decorationBox
    )
}
