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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.R
import com.example.cahier.ui.viewmodels.StylusViewModel
import com.example.cahier.navigation.NavigationDestination
import com.example.cahier.ui.viewmodels.AppViewModelProvider
import com.example.cahier.ui.viewmodels.CanvasScreenViewModel


object NoteCanvasDestination : NavigationDestination {
    override val route = "note_canvas"
    const val NOTE_ID_ARG = "noteId"
    val routeWithArgs = "$route/{$NOTE_ID_ARG}"
}

@Composable
fun NoteCanvas(
    navigateUp: () -> Unit,

    modifier: Modifier = Modifier,
    canvasScreenViewModel: CanvasScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = canvasScreenViewModel.note.collectAsState()
    var isTextFieldVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
        navigateUp()
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
                value = uiState.value.note.title,
                placeholder = { Text(stringResource(R.string.title)) },
                onValueChange = {
                    canvasScreenViewModel.updateUiState(uiState.value.note.copy(title = it))
                    canvasScreenViewModel.updateNoteTitle(it)
                },
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = true,
                    showKeyboardOnFocus = true
                ),
                modifier = Modifier.fillMaxWidth()
            )
            uiState.value.note.text?.let { it ->
                TextField(
                    value = it,
                    placeholder = { Text(stringResource(R.string.note)) },
                    onValueChange = {
                        canvasScreenViewModel.updateUiState(uiState.value.note.copy(text = it))
                        canvasScreenViewModel.updateNoteText(it)
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
