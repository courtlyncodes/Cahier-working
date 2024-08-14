package com.example.cahier.ui

import android.view.MotionEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.R
import com.example.cahier.navigation.NavigationDestination
import com.example.cahier.ui.viewmodels.AppViewModelProvider
import com.example.cahier.ui.viewmodels.CanvasScreenViewModel
import com.example.cahier.ui.viewmodels.StylusViewModel

object NoteCanvasDestination : NavigationDestination {
    override val route = "note_canvas"
    const val NOTE_ID_ARG = "noteId"
    val routeWithArgs = "$route/{$NOTE_ID_ARG}"
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteCanvas(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    canvasScreenViewModel: CanvasScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
    stylusViewModel: StylusViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val uiState = canvasScreenViewModel.note.collectAsState()
    var isTextFieldVisible by remember { mutableStateOf(false) }
    val strokeStyle = Stroke(10F)
    var stylusState by remember { mutableStateOf(StylusState()) }
    val existingPaths = canvasScreenViewModel.existingPaths.collectAsState()

    // When a note is pressed, the noteId is passed to the stylusViewModel
    LaunchedEffect(Unit) {
        canvasScreenViewModel.note.collect{ note ->
            stylusViewModel.setNoteId(note.note.id)
        }
    }

    // When the stylusState changes, update the stylusState in the viewModel
    LaunchedEffect(Unit) {
        stylusViewModel.stylusState.collect { state ->
            stylusState = state
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .pointerInteropFilter {
                when (it.actionMasked) {
                    MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                        // Check if the event is from a stylus
                        if (it.getToolType(0) == MotionEvent.TOOL_TYPE_STYLUS) {
                            stylusViewModel.processMotionEvent(it)
                            true // Consume the event
                        } else {
                            // If not a stylus event, text field should be visible so user can type in the note
                            isTextFieldVisible = true
                            false // Not a stylus event, let it pass through
                        }
                    }
                    else -> false // For other events, let them pass through
                }
            }
    ) {
        // If a stylus is detected, user can draw on canvas
        if (!isTextFieldVisible) {
            // Load existing paths from the database
            existingPaths.value.forEach { path ->
                drawPath(
                    path = path,
                    color = Color(0xFFA4C639),
                    style = strokeStyle
                )
            }
            // Draw the current path
            with(stylusState) {
                drawPath(
                    path = this.path,
                    color = Color(0xFFA4C639),
                    style = strokeStyle
                )
            }
        }
    }
    // User can type in the note
    if (isTextFieldVisible) {
        Column {
            TextField(
                value = uiState.value.note.title,
                placeholder = { Text(stringResource(R.string.title)) },
                onValueChange = {
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

