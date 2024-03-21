package com.example.cahier.ui.stylus

import android.graphics.Path
import android.view.MotionEvent
import androidx.lifecycle.ViewModel
import com.example.cahier.data.stylus.DrawPoint
import com.example.cahier.data.stylus.DrawPointType

class StylusViewModel : ViewModel() {
    private var currentPath = mutableListOf<DrawPoint>()

    fun createPath(): Path {
        val path = Path()

        for(point in currentPath){
          if(point.type === DrawPointType.START){
              path.moveTo(point.x, point.y)
          } else {
              path.lineTo(point.x, point.y)
          }

        }
        return path
    }

    fun processMotionEvent(motionEvent: MotionEvent): Boolean {
        when (motionEvent.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                currentPath.add(
                    DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.START)
                )
            }
            MotionEvent.ACTION_MOVE -> {
                currentPath.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
            }
            MotionEvent.ACTION_UP -> {
                currentPath.add(DrawPoint(motionEvent.x, motionEvent.y, DrawPointType.LINE))
            }
            MotionEvent.ACTION_CANCEL -> {
                // Unwanted touch detected
                cancelLastStroke()
            }
            else -> return false
        }
        return true
    }

    private fun cancelLastStroke() {

    }

}