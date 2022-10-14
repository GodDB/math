package com.example.drivecar

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.cos
import kotlin.math.sin

class MainViewModel : ViewModel() {

    private val _state: MutableStateFlow<Game> = MutableStateFlow(Game.DEFAULT)
    val state: StateFlow<Game> = _state.asStateFlow()

    fun rotate() {
        _state.update {
            val newCar = it.car.rotate(5.0)
            it.copy(
                car = newCar,
                monsters = it.monsters.map {
                    if (newCar.isVisible(it)) {
                        it.copy(color = Color.Cyan)
                    } else {
                        it.copy(color = Color.Green)
                    }
                }
            )
        }
    }

    fun moveFront() {
        val leftScalar = -100.0
        _state.update {
            val newCar = it.car.move(leftScalar)
            it.copy(
                car = newCar,
                monsters = it.monsters.map {
                    if (newCar.isVisible(it)) {
                        it.copy(color = Color.Cyan)
                    } else {
                        it.copy(color = Color.Green)
                    }
                }

            )
        }
    }

    fun moveBack() {
        val rightScalar = 100.0
        _state.update {
            val newCar = it.car.move(rightScalar)
            it.copy(
                car = newCar,
                monsters = it.monsters.map {
                    if (newCar.isVisible(it)) {
                        it.copy(color = Color.Cyan)
                    } else {
                        it.copy(color = Color.Green)
                    }
                }
            )
        }
    }
}

data class Game(
    val car: Car,
    val monsters: List<Monster>
) {

    companion object {
        val DEFAULT: Game = Game(
            car = Car.DEFAULT,
            monsters = Monster.DEFAULT
        )
    }
}

data class Monster(
    val vector2D: Vector2D,
    val color: Color = Color.Green
) {
    companion object {
        val DEFAULT: List<Monster> = List(100) {
            Monster(
                vector2D = Vector2D(10.0 * it + 10, 30.0 * it - 20)
            )
        }
    }
}

data class Car(
    val center: Vector2D,
    val degree: Double,
    val width: Double,
    val height: Double
) {
    val leftTop: Point = kotlin.run {
        val radian = Math.toRadians(degree)
        center
            .plus(-50.0, -50.0)
            .rotate(radian, center.x, center.y)
            .toPoint()
    }

    val rightTop: Point = kotlin.run {
        val radian = Math.toRadians(degree)
        center
            .plus(50.0, -50.0)
            .rotate(radian, center.x, center.y)
            .toPoint()
    }

    val leftBottom: Point = kotlin.run {
        val radian = Math.toRadians(degree)
        center
            .plus(-50.0, 50.0)
            .rotate(radian, center.x, center.y)
            .toPoint()
    }

    val rightBottom: Point = kotlin.run {
        val radian = Math.toRadians(degree)
        center
            .plus(50.0, 50.0)
            .rotate(radian, center.x, center.y)
            .toPoint()
    }

    val front: Point = kotlin.run {
        val radian = Math.toRadians(degree)
        Point(
            center.x + (-50 * cos(radian)),
            center.y + (-50 * sin(radian))
        )
    }

    val frontVisionVector: Vector2D = kotlin.run {
        val radian = Math.toRadians(degree + 180.0)
        Vector2D(
            x = front.x + (100 * cos(radian)),
            y = front.y + (100 * sin(radian))
        )
    }

    val leftVisionPoint: Point = kotlin.run {
        val radian = Math.toRadians(degree + 135.0)
        Point(
            x = front.x + (1000 * cos(radian)),
            y = front.y + (1000 * sin(radian)),
        )
    }
    val rightVisionPoint: Point = kotlin.run {
        val radian = Math.toRadians(degree + 225.0)
        Point(
            x = front.x + (1000 * cos(radian)),
            y = front.y + (1000 * sin(radian)),
        )
    }

    fun move(scalar: Double): Car {
        val radian = Math.toRadians(degree)
        return this.copy(
            center = this.center.addDistance(scalar, radian)
        )
    }

    fun rotate(relativeDegree: Double): Car {
        return this.copy(
            degree = this.degree + relativeDegree,
        )
    }

    fun isVisible(monster: Monster): Boolean {
        //원점을 (0, 0) -> 차의 프론트 포인트로 변경
        val monsterV = monster.vector2D.minus(front.x, front.y)
        val frontV = frontVisionVector.minus(front.x, front.y)
        val monsterUnitV = monsterV.getUnitVector()
        val frontUnitV = frontV.getUnitVector()
        val cosTheta = frontUnitV.dotTimes(monsterUnitV)

        val leftVisionCosTheta = cos(Math.toRadians(45.0))
        return cosTheta > leftVisionCosTheta
    }

    companion object {
        val DEFAULT: Car = Car(
            center = Vector2D(50.0, 50.0),
            degree = 0.0,
            width = 100.0,
            height = 100.0
        )
    }
}

data class Point(val x: Double, val y: Double)
