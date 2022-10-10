package com.example.drivecar

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.cos
import kotlin.math.sin

class MainViewModel : ViewModel() {

    private val _state: MutableStateFlow<Car> = MutableStateFlow(Car.DEFAULT)
    val state: StateFlow<Car> = _state.asStateFlow()

    fun rotate() {
        _state.update {
            it.rotate(5.0)
        }
    }

    fun moveFront() {
        val leftScalar = -100.0
        _state.update {
            it.moveFront(leftScalar)
        }
    }

    fun moveBack() {
        val rightScalar = 100.0
        _state.update {
            it.moveBack(rightScalar)
        }
    }
}

data class Car(
    val leftTop: Vector2D,
    val rightTop: Vector2D,
    val leftBottom: Vector2D,
    val rightBottom: Vector2D,
    val degree: Double
) {
    val center: Point = kotlin.run {
        val xList = arrayOf(leftTop.x, rightTop.x, leftBottom.x, rightBottom.x)
        val yList = arrayOf(leftTop.y, rightTop.y, leftBottom.y, rightBottom.y)
        val minX = xList.minOrNull() ?: 0.0
        val maxX = xList.maxOrNull() ?: 0.0
        val minY = yList.minOrNull() ?: 0.0
        val maxY = yList.maxOrNull() ?: 0.0

        Point(
            minX + (maxX - minX) / 2,
            minY + (maxY - minY) / 2
        )
    }

    val front: Point = kotlin.run {
        val radian = Math.toRadians(degree)
        Point(
            center.x + (-50 * cos(radian)),
            center.y + (-50 * sin(radian))
        )
    }

    fun moveFront(scalar: Double): Car {
        val radian = Math.toRadians(degree)
        return this.copy(
            leftTop = this.leftTop.addDistance(scalar, radian),
            rightTop = this.rightTop.addDistance(scalar, radian),
            leftBottom = this.leftBottom.addDistance(scalar, radian),
            rightBottom = this.rightBottom.addDistance(scalar, radian)
        )
    }

    fun moveBack(scalar: Double): Car {
        val radian = Math.toRadians(degree)
        return this.copy(
            leftTop = this.leftTop.addDistance(scalar, radian),
            rightTop = this.rightTop.addDistance(scalar, radian),
            leftBottom = this.leftBottom.addDistance(scalar, radian),
            rightBottom = this.rightBottom.addDistance(scalar, radian)
        )
    }

    fun rotate(degree: Double): Car {
        val radian = Math.toRadians(degree)
        return this.copy(
            leftTop = this.leftTop.rotate(radian, center.x, center.y),
            rightTop = this.rightTop.rotate(radian, center.x, center.y),
            leftBottom = this.leftBottom.rotate(radian, center.x, center.y),
            rightBottom = this.rightBottom.rotate(radian, center.x, center.y),
            degree = this.degree + degree,
        )
    }

    companion object {
        val DEFAULT: Car = Car(
            leftTop = Vector2D(0.0, 0.0),
            rightTop = Vector2D(100.0, 0.0),
            leftBottom = Vector2D(0.0, 100.0),
            rightBottom = Vector2D(100.0, 100.0),
            degree = 0.0
        )
    }
}

data class Point(val x: Double, val y: Double)
