package com.example.drivecar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color

@Composable
fun CarGame(viewModel: MainViewModel) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        Car(carState = state)

        CarGameZoyStick(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            onClickFront = viewModel::moveFront,
            onClickBack = viewModel::moveBack,
            onClickRotate = viewModel::rotate
        )
    }
}

@Composable
private fun Car(
    carState: Car
) {
    Canvas(modifier = Modifier, onDraw = {


        // top
        drawLine(
            color = Color.Blue,
            start = carState.leftTop.toOffset(),
            end = carState.rightTop.toOffset(),
            strokeWidth = 5f
        )

        // left
        drawLine(
            color = Color.Blue,
            start = carState.leftTop.toOffset(),
            end = carState.leftBottom.toOffset(),
            strokeWidth = 5f
        )

        // right
        drawLine(
            color = Color.Blue,
            start = carState.rightTop.toOffset(),
            end = carState.rightBottom.toOffset(),
            strokeWidth = 5f
        )

        //front
        drawCircle(
            color = Color.Blue,
            radius = 10f,
            center = Offset(carState.front.x.toFloat(), carState.front.y.toFloat())
        )

        //center
        drawCircle(
            color = Color.Black,
            radius = 10f,
            center = Offset(carState.center.x.toFloat(), carState.center.y.toFloat())
        )

        // bottom
        drawLine(
            color = Color.Blue,
            start = carState.leftBottom.toOffset(),
            end = carState.rightBottom.toOffset(),
            strokeWidth = 5f
        )
    })
}

@Composable
private fun CarGameZoyStick(
    modifier: Modifier,
    onClickFront: () -> Unit,
    onClickBack: () -> Unit,
    onClickRotate: () -> Unit
) {
    Column(modifier = modifier) {
        Button(onClick = onClickFront) {
            Text("전진")
        }
        Button(onClick = onClickBack) {
            Text("후진")
        }
        Button(onClick = onClickRotate) {
            Text("회전")
        }
    }
}
