package com.example.beziercurvegraph

import android.util.Log
import android.view.MotionEvent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.tooling.preview.Preview
import com.example.beziercurvegraph.ui.theme.BezierCurveGraphTheme


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BezierCurveGraph(
    modifier: Modifier,
    curve: Curve,
    onTouchP0: (offset: Offset) -> Unit,
    onTouchP1: (offset: Offset) -> Unit,
    onTouchP2: (offset: Offset) -> Unit
) {
    val indicators: List<Indicator> by remember(curve) {
        mutableStateOf(
            listOf(
                curve.p0.toIndicator(),
                curve.p1.toIndicator(),
                curve.p2.toIndicator()
            )
        )
    }

    Surface(
        modifier = modifier
    ) {
        var startPointX by remember {
            mutableStateOf(0f)
        }
        var startPointY by remember {
            mutableStateOf(0f)
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInteropFilter {
                    when (it.action) {
                        MotionEvent.ACTION_DOWN -> {
                            startPointX = it.x
                            startPointY = it.y
                        }

                        MotionEvent.ACTION_MOVE -> {
                            Log.e("godgod", "${it.x - startPointX}")
                            when {
                                indicators[0].contains(it.x, it.y) -> {
                                    Log.e("godgod", "p0 touch")
                                    onTouchP0.invoke(Offset(it.x - startPointX, it.y - startPointY))
                                }
                                indicators[1].contains(it.x, it.y) -> {
                                    Log.e("godgod", "p1 touch")
                                    onTouchP1.invoke(Offset(it.x - startPointX, it.y - startPointY))
                                }
                                indicators[2].contains(it.x, it.y) -> {
                                    Log.e("godgod", "p2 touch")
                                    onTouchP2.invoke(Offset(it.x - startPointX, it.y - startPointY))
                                }
                            }
                            startPointX = it.x
                            startPointY = it.y
                        }

                        MotionEvent.ACTION_UP -> {
                            startPointX = 0f
                            startPointY = 0f
                        }
                    }
                    true
                }
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDrag = { change, dragAmount ->
                            when {
                                indicators[0].contains(change.position.x, change.position.y) -> {
                                    Log.e("godgod", "p0 touch")
                                    onTouchP0.invoke(dragAmount)
                                }
                                indicators[1].contains(change.position.x, change.position.y) -> {
                                    Log.e("godgod", "p1 touch")
                                    onTouchP1.invoke(dragAmount)
                                }
                                indicators[2].contains(change.position.x, change.position.y) -> {
                                    Log.e("godgod", "p2 touch")
                                    onTouchP2.invoke(dragAmount)
                                }
                            }
                        }
                    )
                }
        ) {
            indicators.forEach {
                it.draw(this, Color.Blue)
            }
            drawBezierCurves(curve)
        }
    }
}

var path = Path()

private fun DrawScope.drawBezierCurves(curve: Curve) {
    path.reset()
    path.moveTo(curve.p0.x.toFloat(), curve.p0.y.toFloat())
    path.quadraticBezierTo(
        x1 = curve.p1.x.toFloat(),
        y1 = curve.p1.y.toFloat(),
        x2 = curve.p2.x.toFloat(),
        y2 = curve.p2.y.toFloat()
    )
    drawPath(
        path = path,
        color = Color.Green,
        style = Stroke(width = 5f)
    )

}

@Composable
@Preview
fun PreViewBezierCurveGraph() {
    BezierCurveGraphTheme {
        BezierCurveGraph(
            modifier = Modifier.fillMaxSize(),
            curve = Curve.mockData,
            {}, {}, {}
        )
    }
}
