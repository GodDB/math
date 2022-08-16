package com.example.climbthehillanimation

import android.graphics.Point
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import com.example.climbthehillanimation.ui.theme.ClimbTheHillAnimationTheme

@Composable
fun ClimbTheHillRoute(viewModel: ClimbTheHillViewModel) {
    val state by viewModel.climbTheHillState.collectAsState()
    ClimbTheHillScreen(
        state = state
    )
}

@Composable
fun ClimbTheHillScreen(
    modifier: Modifier = Modifier,
    state: ClimbTheHillState
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier)
    ) {
        ClimbTheHillRenderer(
            modifier = Modifier.fillMaxSize(),
            state = state
        )
    }
}

@Composable
private fun ClimbTheHillRenderer(
    modifier: Modifier,
    state: ClimbTheHillState
) {
    Canvas(modifier = modifier) {
        drawHills(state.hills)
        drawClimber(state.climbers)
    }
}

private val path = Path()

private fun DrawScope.drawHills(hills: List<Hill>) {
    path.reset()
    hills.forEach { hill ->
        hill.points.forEach {
            drawRect(it)
            drawBezierCurve(it, path)
        }
    }
}

private fun DrawScope.drawClimber(climbers: List<Climber>) {
    climbers.forEach {
        val rect = Offset(it.x.toFloat(), it.y.toFloat())
        rotate(it.degree.toFloat(), rect) {
            drawRect(
                color = Color.Green,
                topLeft = rect,
                size = Size(it.width.toFloat(), it.height.toFloat())
            )
        }
    }
}

private fun DrawScope.drawBezierCurve(
    bezierPoint: BezierPoint,
    path: Path
) {
    path.moveTo(bezierPoint.p0.x.toFloat(), bezierPoint.p0.y.toFloat())
    path.quadraticBezierTo(
        x1 = bezierPoint.p1.x.toFloat(),
        y1 = bezierPoint.p1.y.toFloat(),
        x2 = bezierPoint.p2.x.toFloat(),
        y2 = bezierPoint.p2.y.toFloat()
    )
    drawPath(
        path = path,
        color = Color.Green,
        style = Stroke(width = 10f)
    )
}

private fun DrawScope.drawRect(bezierPoint: BezierPoint) {
    drawRectInner(bezierPoint.p0)
    drawRectInner(bezierPoint.p1)
    drawRectInner(bezierPoint.p2)
}

private fun DrawScope.drawRectInner(point: Point) {
    drawRect(
        color = Color.Green,
        topLeft = Offset(point.x.toFloat(), point.y.toFloat()),
        size = Size(50f, 50f)
    )
}


@Preview
@Composable
fun PreviewClimbTheHillScreen() {
    ClimbTheHillAnimationTheme {
        /*   ClimbTheHillScreen()*/
    }
}

