package uk.ac.tees.mad.moneymate.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.database.Expense
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
                expenseRepository.syncExpensesFromFirestore()

                expenseRepository.getAllExpenses().collect { expenses ->
                    val income = expenses.filter { it.isIncome }
                    val expenseList = expenses.filter { !it.isIncome }

                    val totalIncome = income.sumOf { it.amount }
                    val totalExpense = expenseList.sumOf { it.amount }

                    val incomeCategoryData = income.groupBy { it.category }
                        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }
                    val expenseCategoryData = expenseList.groupBy { it.category }
                        .mapValues { (_, expenses) -> expenses.sumOf { it.amount } }

                    val savingsGoal = 1000.0
                    val progress = ((totalIncome - totalExpense) / savingsGoal).coerceAtMost(1.0)

                    _dashboardState.value = DashboardState(
                        totalIncome = totalIncome,
                        totalExpense = totalExpense,
                        incomeCategoryData = incomeCategoryData,
                        expenseCategoryData = expenseCategoryData,
                        recentTransactions = expenses.sortedByDescending { it.date }.take(5),
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

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch {
            try {
                expenseRepository.deleteExpense(expense)
            } catch (e: Exception) {
                _error.value = "Failed to delete expense: ${e.message}"
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
    val incomeCategoryData: Map<String, Double> = emptyMap(),
    val expenseCategoryData: Map<String, Double> = emptyMap(),
    val recentTransactions: List<Expense> = emptyList(),
    val savingsGoal: Double = 1000.0,
    val savingsProgress: Double = 0.0
)