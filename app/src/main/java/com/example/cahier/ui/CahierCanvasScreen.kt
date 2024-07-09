package com.example.cahier.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.example.cahier.R
import com.example.cahier.ui.viewmodels.NoteDetailViewModel
import kotlinx.coroutines.launch

@Composable
fun NoteCanvas(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    noteDetailViewModel: NoteDetailViewModel
) {
    val uiState by noteDetailViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var isTextFieldVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        navigateUp()
        noteDetailViewModel.updateUiState(uiState.note)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(key1 = Unit) {
                detectTapGestures {
                    isTextFieldVisible = true
                }
            }
    ) {
    }
    if (isTextFieldVisible) {
        Column {
            TextField(
                value = uiState.note.title,
                placeholder = { Text(stringResource(R.string.title)) },
                onValueChange = {
                    coroutineScope.launch {
                        noteDetailViewModel.updateNote(uiState.note.copy(title = it))
                    }
                    noteDetailViewModel.updateUiState(uiState.note.copy(title = it))
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = true,
                    showKeyboardOnFocus = true
                ),
                modifier = Modifier.fillMaxWidth()
            )
            uiState.note.text?.let { it ->
                TextField(
                    value = it,
                    placeholder = { Text(stringResource(R.string.note)) },
                    onValueChange = {
                        coroutineScope.launch {
                            noteDetailViewModel.updateNote(uiState.note.copy(text = it))
                        }
                        noteDetailViewModel.updateUiState(uiState.note.copy(text = it))
                    },
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = true,
                        showKeyboardOnFocus = true
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
