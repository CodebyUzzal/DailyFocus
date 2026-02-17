package com.dailyfocus.presentation.notes

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Redo
import androidx.compose.material.icons.outlined.Undo
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.dailyfocus.core.ui.theme.*
import com.dailyfocus.domain.model.NoteBackgroundStyle
import com.dailyfocus.domain.model.NoteContent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditorScreen(
    noteId: Long,
    isChecklist: Boolean,
    onBack: () -> Unit,
    viewModel: NoteEditorViewModel = hiltViewModel()
) {
    LaunchedEffect(noteId) {
        viewModel.loadNote(noteId)
        if (noteId == -1L) {
             viewModel.initNewNote(isChecklist)
        }
    }

    val note by viewModel.currentNote.collectAsState()
    
    // Auto-save on back
    BackHandler {
        viewModel.saveNote()
        onBack()
    }
    
    val bottomSheetState = rememberModalBottomSheetState()
    var showColorPicker by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.saveNote()
                        onBack()
                    }) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = Neutral90)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.deleteNote(); onBack() }) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete",
                            tint = Error40 // Use error color for delete action
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Surface1)
            )
        },

        containerColor = Surface1
    ) { paddingValues ->
        if (note != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
            ) {
                // Title
                item {
                    TextField(
                        value = note!!.title ?: "",
                        onValueChange = { viewModel.updateTitle(it) },
                        placeholder = { Text("Title", style = MaterialTheme.typography.headlineMedium, color = Neutral90.copy(alpha = 0.5f)) },
                        textStyle = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold, color = Neutral90),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                if (note!!.isChecklist) {
                    itemsIndexed(note!!.items, key = { _, item -> item.id.toString() + item.hashCode() }) { index, item ->
                        ChecklistItemRow(
                            item = item,
                            onCheckedChange = { isChecked -> viewModel.updateChecklistItem(index, item.text, isChecked) },
                            onTextChange = { text -> viewModel.updateChecklistItem(index, text, item.isChecked) },
                            onDelete = { viewModel.removeChecklistItem(index) }
                        )
                    }
                    item {
                        // Add Item Row
                        var newItemText by remember { mutableStateOf("") }
                        val focusRequester = remember { FocusRequester() }
                        
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .clickable { focusRequester.requestFocus() }
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = Neutral90.copy(alpha = 0.5f))
                            Spacer(modifier = Modifier.width(12.dp))
                            TextField(
                                value = newItemText,
                                onValueChange = { newItemText = it },
                                placeholder = { Text("List item", color = Neutral90.copy(alpha = 0.5f)) },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        if (newItemText.isNotBlank()) {
                                            viewModel.addChecklistItem(newItemText)
                                            newItemText = ""
                                        }
                                    }
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .focusRequester(focusRequester)
                            )
                        }
                    }
                } else {
                    item {
                        // Single expanding text field
                        val content = note!!.items.firstOrNull()?.text ?: ""
                        TextField(
                            value = content,
                            onValueChange = { viewModel.updateContent(it) },
                            placeholder = { Text("Note", style = MaterialTheme.typography.bodyLarge, color = Neutral90.copy(alpha = 0.5f)) },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = Neutral90, lineHeight = androidx.compose.ui.unit.TextUnit(1.4f, androidx.compose.ui.unit.TextUnitType.Em)),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
        
        if (showColorPicker) {
            ModalBottomSheet(
                onDismissRequest = { showColorPicker = false },
                sheetState = bottomSheetState,
                containerColor = Surface2
            ) {
                 Column(modifier = Modifier.padding(16.dp)) {
                     Text("Background Color", style = MaterialTheme.typography.titleMedium, color = Neutral90)
                     Spacer(modifier = Modifier.height(16.dp))
                     LazyColumn { // Actually horizontal flow row would be better but keeping simple for now
                         // Or just a Row of circles
                         item {
                             Row(
                                 modifier = Modifier.fillMaxWidth(), 
                                 horizontalArrangement = Arrangement.SpaceEvenly
                             ) {
                                 // Simple palette samples
                                 NoteBackgroundStyle.entries.take(5).forEach { style ->
                                     ColorCircle(style) { 
                                         viewModel.updateBackgroundStyle(style) 
                                         scope.launch { bottomSheetState.hide() }.invokeOnCompletion { showColorPicker = false }
                                     }
                                 }
                             }
                             Spacer(modifier = Modifier.height(16.dp))
                             Row(
                                 modifier = Modifier.fillMaxWidth(), 
                                 horizontalArrangement = Arrangement.SpaceEvenly
                             ) {
                                 NoteBackgroundStyle.entries.drop(5).forEach { style ->
                                     ColorCircle(style) {
                                         viewModel.updateBackgroundStyle(style)
                                         scope.launch { bottomSheetState.hide() }.invokeOnCompletion { showColorPicker = false }
                                     }
                                 }
                             }
                         }
                     }
                     Spacer(modifier = Modifier.height(32.dp))
                 }
            }
        }
    }
}

@Composable
fun ColorCircle(style: NoteBackgroundStyle, onClick: () -> Unit) {
    Surface(
        shape = androidx.compose.foundation.shape.CircleShape,
        color = com.dailyfocus.presentation.notes.components.getTintedSurface(style),
        modifier = Modifier.size(48.dp).clickable(onClick = onClick),
        border = androidx.compose.foundation.BorderStroke(1.dp, Neutral90.copy(alpha = 0.2f))
    ) {}
}

@Composable
fun ChecklistItemRow(
    item: NoteContent,
    onCheckedChange: (Boolean) -> Unit,
    onTextChange: (String) -> Unit,
    onDelete: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Icon(
            // Use DragIndicator if possible, else custom icon or DragHandle
            imageVector = Icons.Default.DragHandle, // Fallback for now, could be replaced with specific vector
            contentDescription = "Drag", 
            tint = Neutral90.copy(alpha = 0.3f),
            modifier = Modifier.padding(end = 8.dp)
        )
        
        Checkbox(
            checked = item.isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkedColor = Secondary40, uncheckedColor = Neutral90)
        )
        
        TextField(
            value = item.text,
            onValueChange = onTextChange,
            textStyle = LocalTextStyle.current.copy(
                textDecoration = if (item.isChecked) TextDecoration.LineThrough else null,
                color = if (item.isChecked) Neutral90.copy(alpha = 0.5f) else Neutral90
            ),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Close, contentDescription = "Remove", tint = Neutral90.copy(alpha = 0.5f))
        }
    }
}
