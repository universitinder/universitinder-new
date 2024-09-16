package com.universitinder.app.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
    onSwipedRight: () -> Unit,
    onMiddleClick: () -> Unit,
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
                            if (swipeOffsetX.value > 0) {
                                onSwipedRight()
                            } else {
                                onSwipedLeft()
                            }
                        } else {
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
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { onMiddleClick() }
                )
            }
            .offset { IntOffset(swipeOffsetX.value.toInt(), 0) }
            .background(MaterialTheme.colorScheme.primaryContainer),
        contentAlignment = Alignment.TopCenter
    ) {
        ImageCarousel(imageUris = school.images, onMiddleClick = onMiddleClick)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(10, 10, 10, 130),
                                Color.Transparent
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ){
                    Text(
                        text = school.school?.name ?: "No Institution Name",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                    Text(
                        text = "${school.school?.province}, ${school.school?.municipalityOrCity}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color(10, 10, 10, 130),
                            )
                        )
                    ),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White),
                    ) {
                        Icon(
                            Icons.Filled.Close,
                            modifier = Modifier.padding(12.dp),
                            contentDescription = "Close"
                        )
                    }
                    Text(modifier = Modifier.padding(top= 8.dp), text = "Swipe Left", fontSize = 12.sp, color = Color.White)
                }
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White),
                    ) {
                        Icon(
                            Icons.Filled.Check,
                            modifier = Modifier.padding(12.dp),
                            contentDescription = "Close"
                        )
                    }
                    Text(modifier = Modifier.padding(top= 8.dp), text = "Swipe Right", fontSize = 12.sp, color = Color.White)
                }
            }
        }
    }
}