package com.example.climbthehillanimation

import android.graphics.Point
import android.util.Log
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max

class ClimbTheHillViewModel : ViewModel() {

    private val hillFlow: MutableStateFlow<List<Hill>> = MutableStateFlow(listOf())
    private val climberFlow: MutableStateFlow<List<Climber>> = MutableStateFlow(listOf())

    val climbTheHillState: StateFlow<ClimbTheHillState> = combine(hillFlow, climberFlow) { hills, climbers ->
        ClimbTheHillState(hills, climbers)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ClimbTheHillState(listOf(), listOf())
    )

    init {
        viewModelScope.launch {
            val hills = List(1) {
                Hill.create(MAX_HEIGHT, 200)
            }
            val climbers = List(1) { index ->
                Climber.create(hills[index], 200)
            }
            hillFlow.emit(hills)
            climberFlow.emit(climbers)

            startRender()
        }
    }


    private suspend fun startRender() {
        var time = 0f
        while (true) {
            delay(10)
            time += 0.01f
            climberFlow.update {
                it.map { it.update(time) }
                    .filterNotNull()
            }
            if(time > 1f) time = 0f
        }
    }

    private suspend fun endRender() {

    }

    companion object {
        private const val FPS = 30 // 초당 30프레임
        private const val MAX_HEIGHT = 2000
    }
}

data class ClimbTheHillState(
    val hills: List<Hill>,
    val climbers: List<Climber>
)

data class Hill(
    val points: List<BezierPoint>,
    val speed: Int
) {

    companion object {
        private const val INTERVAL_X = 200

        fun create(maxHeight: Int, speed: Int = 200): Hill {
            var prevPoint = Point(0, 1000)
            var x = 0
            val beziers = List(3) { index ->
                val p1X = x + INTERVAL_X
                val p2X = p1X + INTERVAL_X
                x = p2X
                val p0 = prevPoint
                val p2 = Point(p2X, 1000)
                val p1 = Point(p1X, if (index % 2 == 0) 1500 else 500)
                prevPoint = p2
                BezierPoint(
                    p0 = p0,
                    p1 = p1,
                    p2 = p2
                )
            }
            return Hill(beziers, speed)
        }
    }
}

data class BezierPoint(
    val p0: Point,
    val p1: Point,
    val p2: Point
) {
    /**
     * 0 ~ 1 사이 값을 전달하면 보간 처리한 현재 x, y값을 리턴한다
     */
    fun getPointByFrame(frame: Float): Point {
        return getQuadraticBezier(frame)
    }

    private fun getQuadraticBezier(frame: Float): Point {
        val x = (((1 - frame) * (1 - frame)) * p0.x) + (((2 * frame) * (1 - frame)) * p1.x) + ((frame * frame) * p2.x)
        val y = (((1 - frame) * (1 - frame)) * p0.y) + (((2 * frame) * (1 - frame)) * p1.y) + ((frame * frame) * p2.y)
        return Point(x.toInt(), y.toInt())
    }
}

data class Climber(
    val x: Int,
    val y: Int,
    val targetHill: Hill,
    val speed: Int,
    val currentHillIndex : Int = 0
) {

    /**
     * updateFrame 0 ~ 1
     */
    fun update(time: Float): Climber? {
        Log.e("godgod", "${time}")
        if (currentHillIndex == targetHill.points.lastIndex && time >= 1f) {
            return null
        }
        val targetPoint = targetHill.points[currentHillIndex]
        val newPoint = targetPoint.getPointByFrame(time)
        val newHillIndex = if(time >= 1f) currentHillIndex + 1 else currentHillIndex
        return this.copy(x = newPoint.x, y = newPoint.y, currentHillIndex = newHillIndex)
    }

    companion object {
        fun create(targetHill: Hill, speed: Int): Climber {
            return Climber(
                x = 0,
                y = 0,
                targetHill = targetHill,
                speed = speed
            )
        }
    }
}


