package com.example.drivecar

import androidx.compose.ui.geometry.Offset
import java.lang.Math.atan2
import java.lang.Math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

data class Vector2D(
    val x: Double,
    val y: Double
) {

    operator fun plus(vector: Vector2D): Vector2D {
        return this.copy(
            x = this.x + vector.x,
            y = this.y + vector.y
        )
    }

    operator fun minus(vector: Vector2D): Vector2D {
        return this.copy(
            x = vector.x - this.x,
            y = vector.y - this.y
        )
    }

    operator fun times(scalar: Double): Vector2D {
        return this.copy(
            x = scalar * this.x,
            y = scalar * this.y
        )
    }

    // 원점 기준 (0, 0)기준으로 로테이션 되므로, 사용금지
    @Deprecated("")
    fun rotate(radian: Double): Vector2D {
        val cos = cos(radian)
        val sin = sin(radian)
        return this.copy(
            x = (cos * x) - (sin * y),
            y = (sin * x) + (cos * y)
        )
    }

    // https://gaussian37.github.io/math-la-rotation_matrix/
    fun rotate(radian: Double, targetX: Double, targetY: Double): Vector2D {
        val cos = cos(radian)
        val sin = sin(radian)
        val diffX = x - targetX
        val diffY = y - targetY
        return this.copy(
            x = (cos * diffX) + (-sin * diffY) + targetX,
            y = (sin * diffX) + (cos * diffY) + targetY
        )
    }

    fun addDistance(scalar: Double, radian: Double): Vector2D {
        return this.copy(
            x = this.x + (scalar * cos(radian)),
            y = this.y + (scalar * sin((radian)))
        )
    }

    fun getDistance(): Double {
        return sqrt(x.pow(2) + y.pow(2))
    }

    fun getRadian(): Double {
        return atan2(this.y, this.x)
    }
}

fun Vector2D.toOffset(): Offset {
    return Offset(this.x.toFloat(), this.y.toFloat())
}
