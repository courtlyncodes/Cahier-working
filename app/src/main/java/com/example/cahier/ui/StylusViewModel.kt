package com.example.cahier.ui

import android.os.Build
import android.view.MotionEvent
import androidx.compose.ui.graphics.Path
import androidx.lifecycle.ViewModel
import com.example.cahier.data.DrawPoint
import com.example.cahier.data.DrawPointType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update


class StylusViewModel : ViewModel() {
    private var _stylusState = MutableStateFlow(StylusState())
    val stylusState: StateFlow<StylusState> = _stylusState

    private var currentPath = mutableListOf<DrawPoint>()


    private fun createPath(): Path{
        val path = Path()

        for(point in currentPath){
            if(point.type == DrawPointType.START) {
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
        if(lastIndex > 0) {
            currentPath = currentPath.subList(0, lastIndex - 1)
        }
    }

    fun processMotionEvent(motionEvent: MotionEvent) : Boolean {
        when (motionEvent.actionMasked){
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

    }    }

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