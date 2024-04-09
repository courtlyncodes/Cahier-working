package com.example.cahier.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cahier.ui.theme.CahierTheme

@Composable
fun PracticeCanvas(
    viewModel: CanvasViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var isTextFieldVisible by remember { mutableStateOf(false) }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .pointerInput(key1 = Unit) {
            detectTapGestures {
                isTextFieldVisible = true
            }
        }
//        .pointerInput(key1 = Unit) {
//            detectDragGestures{
//            }
//        }
    ) {
    }

    if (isTextFieldVisible) {
        TextField(
            value = uiState.text,
            onValueChange = { viewModel.updateText(it) },
            modifier = Modifier
                .fillMaxSize()
        )
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
//
