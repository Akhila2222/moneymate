package uk.ac.tees.mad.moneymate.repo

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.moneymate.database.Expense
import uk.ac.tees.mad.moneymate.database.ExpenseDao
import uk.ac.tees.mad.moneymate.firestore.FirestoreDataSource

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val firestoreDataSource: FirestoreDataSource
) {
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    suspend fun addExpense(expense: Expense) {
        expenseDao.insertExpense(expense)
        firestoreDataSource.saveExpenseToFirestore(expense)
    }

    suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense)
        firestoreDataSource.updateExpenseInFirestore(expense)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense)
        firestoreDataSource.deleteExpenseFromFirestore(expense.id)
    }
}
