package com.universitinder.app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.universitinder.app.models.SchoolPlusImages
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun SwipeableCard(
    school: SchoolPlusImages,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit
) {
    val swipeOffsetX = remember { Animatable(0f) }
    val swipeThreshold = 300f
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {
                        if (abs(swipeOffsetX.value) > swipeThreshold) {
                            // Swipe right
                            if (swipeOffsetX.value > 0) {
                                onSwipedRight()
                            } else {
                                onSwipedLeft()
                            }
                        } else {
                            // Reset position if swipe is not far enough
                            coroutineScope.launch {
                                swipeOffsetX.animateTo(
                                    0f,
                                    animationSpec = spring(stiffness = Spring.StiffnessLow)
                                )
                            }
                        }
                    },
                    onDrag = { change, dragAmount ->
                        change.consume()
                        coroutineScope.launch {
                            swipeOffsetX.snapTo(swipeOffsetX.value + dragAmount.x)
                        }
                    }
                )
            }
            .offset { IntOffset(swipeOffsetX.value.toInt(), 0) }
            .padding(16.dp)
//            .paint(painter=rememberAsyncImagePainter(school.images.first()))
            .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(16.dp))
            .fillMaxWidth()
            .height(500.dp),
        contentAlignment = Alignment.Center
    ) {
        Column {
            Text(
                text = school.school?.name ?: "No Institution Name",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}