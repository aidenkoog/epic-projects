package io.github.aidenkoog.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun TripleLayeredQrScanImage() {
    val backgroundImage = painterResource(id = R.drawable.ic_launcher_background)
    val middleImage = painterResource(id = R.drawable.ic_launcher_background)
    val indicatorImage = painterResource(id = R.drawable.ic_launcher_background)
    Box(
        modifier = Modifier
            .width(200.dp)
            .height(200.dp), contentAlignment = Alignment.Center
    ) {
        Image(
            painter = backgroundImage,
            contentDescription = "Background",
            modifier = Modifier
                .graphicsLayer(alpha = 1f)
                .width(200.dp)
                .height(200.dp)
        )
        Image(
            painter = middleImage,
            contentDescription = "QrScanCodeImage",
            modifier = Modifier
                .graphicsLayer(alpha = 1f)
                .width(158.dp)
                .height(158.dp)
        )
        Image(
            painter = indicatorImage,
            contentDescription = "Indicator",
            modifier = Modifier
                .graphicsLayer(alpha = 1f)
                .width(234.dp)
        )
    }
}