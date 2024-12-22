package uk.ac.tees.mad.moneymate.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.moneymate.database.Expense
import java.util.Date

class FirestoreDataSource {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebase = FirebaseAuth.getInstance()
    val currentUser = firebase.currentUser!!
    private val expenseCollection =
        firestore.collection("users").document(currentUser.uid).collection("expenses")

    fun saveExpenseToFirestore(expense: Expense) {
        expenseCollection.document(expense.id.toString())
            .set(expense)
            .addOnSuccessListener {}
            .addOnFailureListener { }
    }

    fun updateExpenseInFirestore(expense: Expense) {
        expenseCollection.document(expense.id.toString())
            .set(expense)
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    fun deleteExpenseFromFirestore(expenseId: Int) {
        expenseCollection.document(expenseId.toString())
            .delete()
            .addOnSuccessListener { }
            .addOnFailureListener { }
    }

    suspend fun getAllExpensesFromFirestore(): List<Expense> = withContext(Dispatchers.IO) {
        try {
            val snapshot = expenseCollection.get().await()
            val data = snapshot.documents.mapNotNull { data ->
                Expense(
                    id = data.getLong("id")?.toInt() ?: 0,
                    amount = data.getDouble("amount") ?: 0.0,
                    date = data.getDate("date") ?: Date(),
                    category = data.getString("category") ?: "",
                    isIncome = data.getBoolean("isIncome") ?: false,
                    attachment = data.getString("attachment"),
                    description = data.getString("description") ?: ""
                )

            }
            return@withContext data
        } catch (e: Exception) {
            Log.e("FirestoreDataSource", "Error fetching expenses", e)
            return@withContext emptyList()
        }
    }
}
