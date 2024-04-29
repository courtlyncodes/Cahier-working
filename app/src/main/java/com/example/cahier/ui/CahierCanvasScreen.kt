package com.example.cahier.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
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
import com.example.cahier.data.CanvasUiState
import com.example.cahier.data.Note

@Composable
fun PracticeCanvas(
    note: Note,
    onValueChange: (Note) -> Unit = {},
    onNavigateUp: (Note) -> Unit,
    viewModel: CanvasViewModel = viewModel(),
    newViewModel: DaoViewModel = viewModel(factory = AppViewModelProvider.Factory),
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    var isTextFieldVisible by remember { mutableStateOf(false) }

    BackHandler(enabled = true) {
       onNavigateUp(note)
    }

    Canvas(modifier = modifier
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
            note.text?.let {
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


@Composable
fun CanvasWrapper(canvasUiState: CanvasUiState, onValueChange: (Note) -> Unit, onNavigateUp: (Note) -> Unit) {
    Scaffold() {
        PracticeCanvas(note = canvasUiState.note, onValueChange = onValueChange, onNavigateUp = onNavigateUp, modifier = Modifier.padding(it))
    }
}


//
//    Canvas(modifier = Modifier
//        .fillMaxSize()
//        // Handle pointer input events on the canvas
//        // It detects drag gestures where the user drags their pointer across the canvas
//        .pointerInput(key1 = true, key2 = null)
//        {
//            // Lambda is called when a drag gesture is detected
//            // The lambda creates a Line object representing the segment of the line being drawn and add it to the lines list
//            detectDragGestures { change, dragAmount ->
//                change.consume() // deprecated?
//
//                val line = Line(
//                    start = change.position - dragAmount,
//                    end = change.position
//                )
//                lines.add(line)
//            }
//        }
//    ) {
//        // Loop through each Line in the lines list and draw them using the drawLine function
//        lines.forEach { line ->
//                drawLine(
//                    color = line.color,
//                    start = line.start,
//                    end = line.end,
//                    strokeWidth = line.strokeWidth.toPx(),
//                    cap = StrokeCap.Round
//                )
//        }
//    }
//}


//data class Line(
//    val start: Offset,
//    val end: Offset,
//    val color: Color = Color(0xFFFF6Ec7),
//    val strokeWidth: Dp = 1.dp
//)

//@Preview(showBackground = true)
//@Composable
//fun CahierHomeScreenPreview() {
//    CahierTheme {
//        PracticeCanvas()
//    }
//}

