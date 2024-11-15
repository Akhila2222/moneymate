package uk.ac.tees.mad.moneymate.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import uk.ac.tees.mad.moneymate.database.Expense
import uk.ac.tees.mad.moneymate.database.ExpenseDao
import uk.ac.tees.mad.moneymate.firestore.FirestoreDataSource
import uk.ac.tees.mad.moneymate.firestore.StorageDataSource
import javax.inject.Inject

class ExpenseRepository @Inject constructor(
    private val expenseDao: ExpenseDao,
    private val firestoreDataSource: FirestoreDataSource
) {
    fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses()
    }

    suspend fun syncExpensesFromFirestore() {
        val localExpenses = expenseDao.getAllExpenses().first()
        if (localExpenses.isEmpty()) {
            val firestoreExpenses = firestoreDataSource.getAllExpensesFromFirestore()
            firestoreExpenses.forEach { expense ->
                expenseDao.insertExpense(expense)
            }
        }
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