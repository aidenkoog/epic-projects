package io.github.aidenkoog.compose

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun AddTexts() {
    val names = listOf("Test1", "Test2")
    for (name in names) {
        Text(text = name)
    }
}