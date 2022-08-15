package com.example.climbthehillanimation

import android.graphics.Point
import android.util.Log
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.random.Random

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
            Log.e("godgod", "$hills")
            hillFlow.emit(hills)
        }

    }


    private suspend fun startRender() {
        viewModelScope.launch {

            startRender()
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
    val points : List<Point>,
    val speed: Int
) {

    companion object {
        private const val INTERVAL_X = 200

        fun create(maxHeight: Int, speed : Int = 200): Hill {
            val heightRange = (0 .. maxHeight)
            return Hill(
                points = List(size = 10) { index ->
                    val x = INTERVAL_X * index
                    Point(x, heightRange.random())
                },
                speed = speed
            )
        }
    }
}

data class Climber(
    val x: Int,
    val y: Int,
    val targetHill: Hill,
    val speed: Int
)


