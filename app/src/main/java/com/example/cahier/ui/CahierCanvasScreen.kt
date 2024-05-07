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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import com.example.cahier.R
import com.example.cahier.data.Note

@Composable
fun NoteCanvas(
    note: Note,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    onValueChange: (Note) -> Unit = {}
) {
    var isTextFieldVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        onNavigateUp()
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
                value = note.title,
                placeholder = { Text(stringResource(R.string.title)) },
                onValueChange = {
                    onValueChange(note.copy(title = it))
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrect = true,
                    shouldShowKeyboardOnFocus = true
                ),
                modifier = Modifier.fillMaxWidth()
            )
            note.text?.let { it ->
                TextField(
                    value = it,
                    placeholder = { Text(stringResource(R.string.note)) },
                    onValueChange = { onValueChange(note.copy(text = it)) },
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = true,
                        shouldShowKeyboardOnFocus = true
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
        }
    }
}
