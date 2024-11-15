package uk.ac.tees.mad.moneymate.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.repo.ExpenseRepository
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _dashboardState = MutableStateFlow(DashboardState())
    val dashboardState = _dashboardState.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        fetchDashboardData()
    }

    private fun fetchDashboardData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // First, try to sync data from Firestore if the local database is empty
                expenseRepository.syncExpensesFromFirestore()

                // Now, collect expenses from the local database
                expenseRepository.getAllExpenses().collect { expenses ->
                    val income = expenses.filter { it.isIncome }.sumOf { it.amount }
                    val expense = expenses.filter { !it.isIncome }.sumOf { it.amount }
                    val categoryData = expenses.groupBy { it.category }
                        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
                    val savingsGoal = 1000.0 // This could be fetched from user preferences
                    val progress = ((income - expense) / savingsGoal).coerceAtMost(1.0)

                    _dashboardState.value = DashboardState(
                        totalIncome = income,
                        totalExpense = expense,
                        categoryData = categoryData,
                        savingsGoal = savingsGoal,
                        savingsProgress = progress
                    )
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Failed to fetch data: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}

data class DashboardState(
    val totalIncome: Double = 0.0,
    val totalExpense: Double = 0.0,
    val categoryData: Map<String, Double> = emptyMap(),
    val savingsGoal: Double = 1000.0,
    val savingsProgress: Double = 0.0
)