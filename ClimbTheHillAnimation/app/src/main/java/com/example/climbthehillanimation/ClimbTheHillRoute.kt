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
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
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
        drawHills(state.hills, Paint().apply {
            isAntiAlias = true
            strokeWidth = 10f
            style = PaintingStyle.Stroke
            color = Color.Black
        })
    }
}


private fun DrawScope.drawHills(hills: List<Hill>, paint: Paint) {
    val path = Path()
    hills.forEach { hill ->
        var prevPoint = hill.points[0]
        drawRect(Color.Green, Offset(prevPoint.x.toFloat(), prevPoint.y.toFloat()), size = Size(50f, 50f))
        for (i in 1 until hill.points.size) {
            val curPoint = hill.points[i]
            val midPoint = Point((prevPoint.x + curPoint.x) / 2, (prevPoint.y + curPoint.y) / 2)
            path.quadraticBezierTo(prevPoint.x.toFloat(), prevPoint.y.toFloat(), midPoint.x.toFloat(), midPoint.y.toFloat())
            this.drawContext.canvas.drawPath(path, paint)
            drawRect(Color.Green, Offset(curPoint.x.toFloat(), curPoint.y.toFloat()), size = Size(50f, 50f)) //원래 포인트 그린
            drawRect(Color.Blue, Offset(midPoint.x.toFloat(), midPoint.y.toFloat()), size = Size(50f, 50f)) // 보간 처리된 블루
            prevPoint = curPoint
        }

    }
}


@Preview
@Composable
fun PreviewClimbTheHillScreen() {
    ClimbTheHillAnimationTheme {
        /*   ClimbTheHillScreen()*/
    }
}

