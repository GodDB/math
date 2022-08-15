package com.example.beziercurvegraph

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope

data class Curve(
    val p0: IntPoint,
    val p1: IntPoint,
    val p2: IntPoint
) {
    companion object {
        val mockData: Curve = Curve(
            p0 = IntPoint(500, 500),
            p1 = IntPoint(700, 1000),
            p2 = IntPoint(900, 500)
        )
    }
}

data class IntPoint(
    val x: Int,
    val y: Int
) {
    fun update(offset: Offset): IntPoint {
        return IntPoint(x = (x + offset.x).toInt(), y = (y + offset.y).toInt())
    }
}

fun IntPoint.toIndicator(
    sizeWidth: Int = 200,
    sizeHeight: Int = 200
): Indicator = Indicator(this.x, this.y, sizeWidth, sizeHeight)

data class Indicator(
    val x: Int,
    val y: Int,
    val sizeWidth: Int,
    val sizeHeight: Int
) {
    fun contains(touchedX: Float, touchedY: Float): Boolean {
        return (touchedX >= x && sizeWidth + x >= touchedX) && (touchedY >= y && sizeHeight + y >= touchedY)
    }
}

fun Indicator.draw(
    drawScope: DrawScope,
    color: Color
) {
    with(drawScope) {
        drawRect(
            color = color,
            topLeft = Offset(x.toFloat(), y.toFloat()),
            size = Size(sizeWidth.toFloat(), sizeHeight.toFloat())
        )
    }
}
