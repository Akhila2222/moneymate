package uk.ac.tees.mad.moneymate.presentation.expenseentry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun addExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.addExpense(expense)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to add expense: ${e.message}"
            }
        }
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

    fun clearError() {
        _error.value = null
    }
}
