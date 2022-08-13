package com.example.climbthehillanimation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.climbthehillanimation.ui.theme.ClimbTheHillAnimationTheme

@Composable
fun ClimbTheHillRoute(viewModel : ClimbTheHillViewModel) {
    ClimbTheHillScreen()
}

@Composable
fun ClimbTheHillScreen() {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

    }
}


@Preview
@Composable
fun PreviewClimbTheHillScreen() {
    ClimbTheHillAnimationTheme {
        ClimbTheHillScreen()
    }
}

