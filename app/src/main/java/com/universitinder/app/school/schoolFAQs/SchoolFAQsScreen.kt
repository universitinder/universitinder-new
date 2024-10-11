package com.universitinder.app.school.schoolFAQs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.universitinder.app.models.FAQ

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchoolFAQsScreen(schoolFAQsViewModel: SchoolFAQsViewModel) {
    val uiState by schoolFAQsViewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(
                        onClick = { schoolFAQsViewModel.popActivity() }
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go Back")
                    }
                },
                title = { Text(text = "Frequently Asked Questions") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = schoolFAQsViewModel::startAddFaqActivity,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add")
            }
        }
    ){ innerPadding ->
        when (uiState.fetchingLoading) {
            true -> {
                Box(modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            false -> {
                when (uiState.faqs.size) {
                    0 -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = "No FAQs Available")
                        }
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            itemsIndexed(uiState.faqs) { index, it ->
                                val faq = it.toObject(FAQ::class.java)
                                if (faq != null) {
                                    ListItem(
                                        modifier = Modifier.fillMaxWidth().clickable { schoolFAQsViewModel.startEditFaqActivity(it.id) },
                                        leadingContent = { Text(text = (index + 1).toString()) },
                                        headlineContent = { Text(text = if (faq.question.length > 20) "${faq.question.substring(0, 20)}..." else faq.question) },
                                        trailingContent = { Icon(Icons.Filled.KeyboardArrowRight, contentDescription = "Go Right") }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}