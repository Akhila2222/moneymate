package uk.ac.tees.mad.moneymate.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    @Query("SELECT * FROM expenses ORDER BY date DESC")
    fun getAllExpenses(): Flow<List<Expense>>

    @Upsert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses WHERE id=:expenseId")
    fun getExpenseById(expenseId: Long): Expense

    @Delete
    suspend fun deleteExpense(expense: Expense)
}
