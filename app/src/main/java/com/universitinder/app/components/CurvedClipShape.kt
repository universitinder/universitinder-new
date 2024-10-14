package com.universitinder.app.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CurvedClipShape() {
    val curvedShape = GenericShape { size, _ ->
        moveTo(0f, size.height)
        quadraticBezierTo(
            size.width / 2, size.height - 200f,  // Control point
            size.width, size.height  // End point
        )
        lineTo(size.width, 0f)
        lineTo(0f, 0f)
        close()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.primary, shape = curvedShape)
    )
}