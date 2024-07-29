package com.example.cahier.ui

import android.view.MotionEvent
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.stringResource
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
    canvasScreenViewModel: CanvasScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState = canvasScreenViewModel.note.collectAsState()
    var isTextFieldVisible by remember { mutableStateOf(false) }
    val viewModel: StylusViewModel = viewModel()
    val strokeStyle = Stroke(10F)
    var stylusState by remember { mutableStateOf(StylusState()) }

    LaunchedEffect(Unit) {
        viewModel.stylusState.collect { state ->
            stylusState = state
        }
    }

    BackHandler(enabled = true) {
        navigateUp()
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
                            viewModel.processMotionEvent(it)
                            true // Consume the event
                        } else {
                            isTextFieldVisible = true
                            false // Not a stylus event, let it pass through
                        }
                    }

                    else -> false // For other events, let them pass through
                }
            }
//            .pointerInput(key1 = Unit) {
//                detectTapGestures {
//                    isTextFieldVisible = false
//                }
//            }
    ) {
        if (!isTextFieldVisible) {
            with(stylusState) {
                drawPath(
                    path = this.path,
                    color = Color.Magenta,
                    style = strokeStyle
                )
            }
        }
    }
    if (!isTextFieldVisible) {
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
