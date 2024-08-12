package com.example.cahier.ui.viewmodels

import android.os.Build
import android.util.Log
import android.view.MotionEvent
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cahier.data.Converters
import com.example.cahier.data.DrawPoint
import com.example.cahier.data.DrawPointType
import com.example.cahier.data.NotesRepository
import com.example.cahier.data.OfflineNotesRepository
import com.example.cahier.ui.StylusState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class StylusViewModel(private val notesRepository: NotesRepository) : ViewModel() {
    private var _stylusState = MutableStateFlow(StylusState())
    val stylusState: StateFlow<StylusState> = _stylusState

    private var currentPath = mutableListOf<DrawPoint>()

    private val paths = mutableListOf(Path())

    private var noteId: Long? = null

    fun setNoteId(currentNoteId: Long) {
        noteId = currentNoteId
    }


    private fun createPath(): Path {
        val path = Path()

        for (point in currentPath) {
            if (point.type == DrawPointType.START) {
                path.moveTo(point.x, point.y)
            } else {
                path.lineTo(point.x, point.y)
            }
        }
        return path
    }

    private fun cancelLastStroke() {
        //find the last START event
        val lastIndex = currentPath.findLastIndex {
            it.type == DrawPointType.START
        }

        // If found, keep the element from 0 until the very last event before the last MOVE event
        if (lastIndex > 0) {
            currentPath = currentPath.subList(0, lastIndex - 1)
        }
    }

    fun processMotionEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentPath.add(
                    DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.START)
                )
            }

            MotionEvent.ACTION_MOVE -> {
                currentPath.add(
                    DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE)
                )
            }

            MotionEvent.ACTION_UP -> {
                val canceled =
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && (motionEvent.flags and MotionEvent.FLAG_CANCELED) == MotionEvent.FLAG_CANCELED
                if (canceled) {
                    cancelLastStroke()
                } else {
                    currentPath.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
                }
                saveDrawing()
            }

            MotionEvent.ACTION_CANCEL -> {
                cancelLastStroke()
            }

            else -> return false
        }
        requestRendering(
            StylusState(
                tilt = motionEvent.getAxisValue(MotionEvent.AXIS_TILT),
                pressure = motionEvent.pressure,
                orientation = motionEvent.orientation,
                path = createPath()
            )
        )
        return true
    }

    private fun requestRendering(stylusState: StylusState) {
        _stylusState.update {
            return@update stylusState
        }
    }

    private fun addPointToCurrentPath(x: Float, y: Float, type: DrawPointType) {
        currentPath.add(DrawPoint(x, y, type))
    }

    private fun saveDrawing() {
        viewModelScope.launch {
            noteId?.let { id ->
                val path = createPath()
                notesRepository.addDrawing(id, path)
            }
        }
    }
}

inline fun <T> List<T>.findLastIndex(predicate: (T) -> Boolean): Int {
    val iterator = this.listIterator(size)
    var count = 1
    while (iterator.hasPrevious()) {
        val element = iterator.previous()
        if (predicate(element)) {
            return size - count
        }
        count++
    }
    return -1
}