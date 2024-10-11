package com.universitinder.app.school.schoolFAQs.createFAQ

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.universitinder.app.controllers.FaqController
import com.universitinder.app.models.FAQ
import com.universitinder.app.models.ResultMessage
import com.universitinder.app.models.ResultMessageType
import com.universitinder.app.models.School
import com.universitinder.app.models.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreateFAQViewModel(
    private val school: School,
    private val faqController: FaqController,
    val popActivity: () -> Unit,
) : ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(CreateFAQUiState())
    val uiState : StateFlow<CreateFAQUiState> = _uiState.asStateFlow()

    fun onQuestionChange(newVal: String) { _uiState.value = _uiState.value.copy(question = newVal) }
    fun onAnswerChange(newVal: String) { _uiState.value = _uiState.value.copy(answer = newVal) }

    private fun fieldsNotFilled() : Boolean {
        return _uiState.value.answer.isEmpty() || _uiState.value.answer.isBlank() || _uiState.value.question.isEmpty() || _uiState.value.question.isBlank()
    }

    fun createFAQ() {
        if (fieldsNotFilled()) {
            _uiState.value = _uiState.value.copy(
                resultMessage = ResultMessage(
                    show = true,
                    message = "Please fill in all the fields",
                    type = ResultMessageType.FAILED
                )
            )
            return
        }
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(createLoading = true) }
                val result = faqController.createFAQ(schoolID = school.documentID, faq = FAQ(question = _uiState.value.question, answer = _uiState.value.answer))
                withContext(Dispatchers.Main) {
                    if (result) {
                        _uiState.value = _uiState.value.copy(
                            createLoading = false,
                            resultMessage = ResultMessage(
                                type = ResultMessageType.SUCCESS,
                                show = true,
                                message = "Successfully added new question"
                            )
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            createLoading = false,
                            resultMessage = ResultMessage(
                                type = ResultMessageType.FAILED,
                                show = true,
                                message = "Adding new question unsuccessful"
                            )
                        )
                    }
                }
            }
        }
    }
}