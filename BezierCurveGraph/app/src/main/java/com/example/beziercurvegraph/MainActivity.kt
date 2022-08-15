package com.example.beziercurvegraph

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.beziercurvegraph.ui.theme.BezierCurveGraphTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BezierCurveGraphTheme {
                var state by remember {
                    mutableStateOf(Curve.mockData)
                }
                BezierCurveGraph(
                    modifier = Modifier.fillMaxSize(),
                    curve = state,
                    onTouchP0 = { offset ->
                        state = state.copy(p0 = state.p0.update(offset))
                    },
                    onTouchP1 = { offset ->
                        state = state.copy(p1 = state.p1.update(offset))
                    },
                    onTouchP2 = { offset ->
                        state = state.copy(p2 = state.p2.update(offset))
                    }
                )
            }
        }
    }
}

