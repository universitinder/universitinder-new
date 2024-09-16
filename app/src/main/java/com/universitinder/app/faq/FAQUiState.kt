package com.universitinder.app.faq

import com.google.firebase.firestore.DocumentSnapshot
import com.universitinder.app.models.FAQ

enum class MessagesFrom {
    SYSTEM,
    USER
}

data class Message(
    val from: MessagesFrom = MessagesFrom.SYSTEM,
    val message: String
)

data class FAQUiState(
    val fetchingLoading: Boolean = false,
    val faqs: List<DocumentSnapshot> = listOf(),
    val faqObjects: List<FAQ> = listOf(),
    val messages: List<Message> = listOf(),
    val userMessage: String = ""
)
