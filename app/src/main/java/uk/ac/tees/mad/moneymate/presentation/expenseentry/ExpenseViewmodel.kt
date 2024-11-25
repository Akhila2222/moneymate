package uk.ac.tees.mad.moneymate.presentation.expenseentry

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.database.Expense
import uk.ac.tees.mad.moneymate.repo.ExpenseRepository
import javax.inject.Inject

@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses = _expenses.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _attachmentUri = MutableStateFlow<Uri?>(null)
    val attachmentUri = _attachmentUri.asStateFlow()

    fun getAllExpenses() {
        viewModelScope.launch {
            try {
                expenseRepository.getAllExpenses()
                    .collect { expensesList ->
                        _expenses.value = expensesList
                    }
            } catch (e: Exception) {
                _error.value = "Failed to load expenses: ${e.message}"
            }
        }
    }

    fun addExpense(expense: Expense, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                expenseRepository.addExpense(expense, onSuccess = onSuccess)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to add expense: ${e.message}"
            }
        }
    }

     fun getExpenseById(expenseId: Long): Expense {
        return expenseRepository.getExpenseById(expenseId)
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.updateExpense(expense)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to update expense: ${e.message}"
            }
        }
    }

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to delete expense: ${e.message}"
            }
        }
    }

    fun setAttachmentUri(uri: Uri?) {
        _attachmentUri.value = uri
    }

    fun clearError() {
        _error.value = null
    }
}
