package com.example.cahier.data

import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.vector.PathParser
import androidx.room.TypeConverter
import com.example.cahier.ui.StylusState
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



// Type Converter to convert path to string and back
// Ask Chris about this

class Converters {

    @TypeConverter
    fun fromPath(path: Path?): String? {
        if (path == null) return null
        val points = mutableListOf<DrawPoint>()
        path.forEachSegment { x, y, type ->
            points.add(DrawPoint(x, y, type))
        }
        return Gson().toJson(points)
    }
    @TypeConverter
    fun toPath(pathString: String?): Path? {
        if (pathString.isNullOrEmpty()) return null
        val type = object : TypeToken<List<DrawPoint>>() {}.type
        val points: List<DrawPoint> = Gson().fromJson(pathString, type)
        val path = Path()
        points.forEach { point ->
            when (point.type) {
                DrawPointType.START -> path.moveTo(point.x, point.y)
                DrawPointType.LINE -> path.lineTo(point.x, point.y)
                DrawPointType.FINISH -> {
                    path.lineTo(point.x, point.y)
                    path.close()
                }
            }
        }
        return path
    }
}
fun Path.forEachSegment(action: (x: Float, y: Float, type: DrawPointType) -> Unit) {
    val pathAndroid = this.asAndroidPath()
    val pathMeasure = android.graphics.PathMeasure(pathAndroid, false)
    val pos = FloatArray(2)
    while (pathMeasure.nextContour()) {
        var distance = 0f
        while (distance < pathMeasure.length) {
            pathMeasure.getPosTan(distance, pos, null)
            action(pos[0], pos[1], DrawPointType.LINE)
            distance += 1f
        }
    }
}