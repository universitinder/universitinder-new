package com.universitinder.app.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(imageUris: List<Uri>) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {

        HorizontalPager(
            userScrollEnabled = false,
            count = imageUris.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(0.dp)
        ) { page ->
            Image(
                painter = rememberImagePainter(data = imageUris[page]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(top = 72.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.White else Color.Transparent
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(if (pagerState.currentPage == iteration) RoundedCornerShape(12.dp) else CircleShape)
                        .border(2.5.dp, color = Color.White)
                        .background(color)
                        .size(if (pagerState.currentPage == iteration) (28.dp) else 12.dp, 12.dp)
                        .clickable {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(iteration)
                            }
                        }
                )
            }
        }
    }
}