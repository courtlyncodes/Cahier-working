package com.example.cahier.ui

import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.R
import com.example.cahier.ui.viewmodels.NoteDetailViewModel
import com.example.cahier.ui.viewmodels.StylusViewModel
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NoteCanvas(
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    noteDetailViewModel: NoteDetailViewModel
) {
    val uiState by noteDetailViewModel.uiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
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
        noteDetailViewModel.updateUiState(uiState.note)
    }

        Canvas(
            modifier = modifier
                .clipToBounds()
                .fillMaxSize()
                .pointerInteropFilter {
                    viewModel.processMotionEvent(it)
                }
                .pointerInput(key1 = Unit) {
                    detectTapGestures {
                        isTextFieldVisible = true
                    }
                }
        ) {
            with(stylusState) {
                drawPath(
                    path = this.path,
                    color = Color.Magenta,
                    style = strokeStyle
                )
            }
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
