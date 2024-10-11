package com.universitinder.app.school.schoolFAQs.editFAQ

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

class EditFAQViewModel(
    private val school : School,
    private val documentID: String,
    private val faqController: FaqController,
    val popActivity: () -> Unit
): ViewModel() {
    private val currentUser = UserState.currentUser
    private val _uiState = MutableStateFlow(EditFAQUiState())
    val uiState: StateFlow<EditFAQUiState> = _uiState.asStateFlow()

    init {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(fetchingLoading = true) }
                val faq = faqController.getFAQ(school.documentID, documentID = documentID)
                if (faq != null) {
                    _uiState.value = _uiState.value.copy(
                        fetchingLoading = false,
                        question = faq.question,
                        answer = faq.answer
                    )
                }
            }
        }
    }

    fun onQuestionChange(newVal: String) { _uiState.value = _uiState.value.copy(question = newVal) }
    fun onAnswerChange(newVal: String) { _uiState.value = _uiState.value.copy(answer = newVal) }
    fun onDeleteDialogToggle() { _uiState.value = _uiState.value.copy(showDeleteDialog = !_uiState.value.showDeleteDialog) }

    private fun fieldsNotFilled(): Boolean {
        return _uiState.value.answer.isEmpty() || _uiState.value.answer.isBlank() || _uiState.value.question.isEmpty() || _uiState.value.question.isBlank()
    }

    fun updateFAQ() {
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
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(updateLoading = true)
                }
                val result = faqController.updateFAQ(
                    schoolID = school.documentID,
                    documentID = documentID,
                    faq = FAQ(question = _uiState.value.question, answer = _uiState.value.answer)
                )
                withContext(Dispatchers.Main) {
                    if (result) {
                        _uiState.value = _uiState.value.copy(
                            updateLoading = false,
                            resultMessage = ResultMessage(
                                type = ResultMessageType.SUCCESS,
                                show = true,
                                message = "Successfully updated question information"
                            )
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            updateLoading = false,
                            resultMessage = ResultMessage(
                                type = ResultMessageType.FAILED,
                                show = true,
                                message = "Updating question information unsuccessful"
                            )
                        )
                    }
                }
            }
        }
    }

    fun deleteFAQ() {
        if (currentUser != null) {
            viewModelScope.launch(Dispatchers.IO) {
                withContext(Dispatchers.Main) { _uiState.value = _uiState.value.copy(deleteLoading = true) }
                val result = faqController.deleteFAQ(schoolID = school.documentID, documentID = documentID)
                if (result) {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            deleteResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.SUCCESS,
                                message = "Successfully deleted course"
                            ),
                            deleteLoading = false
                        )
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            deleteResultMessage = ResultMessage(
                                show = true,
                                type = ResultMessageType.FAILED,
                                message = "Course deletion unsuccessful"
                            ),
                            deleteLoading = false
                        )
                    }
                }
            }
        }
    }
}