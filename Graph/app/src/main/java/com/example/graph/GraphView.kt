package com.example.graph

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat

@Composable
fun GraphView(
    modifier: Modifier,
    points: List<GraphPoint>,
    isAnim: Boolean = false
) {
    val context = LocalContext.current
    val paddingX = with(LocalDensity.current) {
        20.dp.toPx()
    }
    val paint = remember {
        Paint().apply {
            isAntiAlias = true
            color = ContextCompat.getColor(context, R.color.black)
            strokeWidth = 10f
            textSize = 20f
        }
    }

    val normalizedPoints by remember(points) {
        mutableStateOf(
            points.map {
                it.copy(x = it.x + paddingX)
            }
        )
    }

    val control by rememberBezierControlPair(pointList = normalizedPoints)

    Canvas(
        modifier = modifier,
        onDraw = {
            drawXBaseline(
                paint = paint,
                paddingSpace = paddingX
            )
            drawYBaseline(
                paint = paint
            )
            drawGraph(
                points = normalizedPoints,
                control1 = control.first,
                control2 = control.second
            )
            drawPoint(
                points = normalizedPoints
            )
        }
    )
}

private fun DrawScope.drawXBaseline(
    paint: Paint,
    paddingSpace: Float,
) {
    val parentWidth = size.width
    var x = paddingSpace
    var displayX = 0
    while (x <= parentWidth) {
        drawIntoCanvas {
            it.nativeCanvas.drawText("$displayX", x, size.height, paint)
        }
        displayX += 50
        x += 50
    }
}

private fun DrawScope.drawYBaseline(
    paint: Paint
) {
    val parentHeight = size.height
    var y = 0f
    var displayY = 0
    while (y <= parentHeight) {
        drawIntoCanvas {
            it.nativeCanvas.drawText("$displayY", 0f, y, paint)
        }
        displayY += 50
        y += 50
    }
}

private fun DrawScope.drawPoint(
    points: List<GraphPoint>
) {
    points.forEach {
        drawCircle(
            color = Color.Green,
            radius = 10f,
            center = Offset(it.x, it.y)
        )
    }
}

private fun DrawScope.drawGraph(
    points: List<GraphPoint>,
    control1: List<GraphPoint>,
    control2: List<GraphPoint>
) {
    val path = Path().apply {
        // move to 주의사항
        // path로 그릴 때 moveTo를 최대한 지양해야함. 최초 한번만 부르고, 그다음은 계속 연결된 선으로 진행하는게 좋음
        // 매번 loop마다 moveTo를 사용하게 된다면, 끊어진 선이라고 지정하게 되는 것임
        this.moveTo(points.first().x, points.first().y)
        for (index in 0 until points.size - 1) {
            val endGraphPoint = points[index + 1] // 끝점
            val controlGraphPoint1 = control1[index] // 조절점 1
            val controlGraphPoint2 = control2[index] // 조절점 2
            this.cubicTo(
                x1 = controlGraphPoint1.x,
                y1 = controlGraphPoint1.y, // 조절점 1
                x2 = controlGraphPoint2.x,
                y2 = controlGraphPoint2.y, // 조절점 2
                x3 = endGraphPoint.x,
                y3 = endGraphPoint.y
            )
        }
    }

    val fillPath = android.graphics.Path(path.asAndroidPath())
        .asComposePath()
        .apply {
            lineTo(points.last().x, size.height)
            lineTo(points.first().x, size.height)
            close()
        }
    drawPath(
        path = fillPath,
        brush = Brush.verticalGradient(
            listOf(
                Color.Cyan,
                Color.Transparent,
            ),
            endY = size.height
        ),
    )

    drawPath(
        path = path,
        color = Color.Black,
        style = Stroke(
            width = 5f,
            cap = StrokeCap.Round
        )
    )
}

@Composable
private fun rememberBezierControlPair(
    pointList: List<GraphPoint>
): State<Pair<List<GraphPoint>, List<GraphPoint>>> {
    return remember(pointList) {
        val bezierControl1 = mutableListOf<GraphPoint>()
        val bezierControl2 = mutableListOf<GraphPoint>()

        for (i in 1 until pointList.size) {
            bezierControl1.add(
                GraphPoint(
                    x = (pointList[i].x + pointList[i - 1].x) / 2,
                    y = pointList[i - 1].y
                )
            )
            bezierControl2.add(
                GraphPoint(
                    x = (pointList[i].x + pointList[i - 1].x) / 2,
                    y = pointList[i].y
                )
            )
        }

        mutableStateOf(bezierControl1 to bezierControl2)
    }
}

@Preview
@Composable
fun PreviewGraphView() {

}

data class GraphPoint(
    val x: Float,
    val y: Float
)
