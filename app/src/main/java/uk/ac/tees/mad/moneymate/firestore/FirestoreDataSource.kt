package uk.ac.tees.mad.moneymate.firestore

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import uk.ac.tees.mad.moneymate.database.Expense

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
            return@withContext snapshot.toObjects(Expense::class.java)
        } catch (e: Exception) {
            Log.e("FirestoreDataSource", "Error fetching expenses", e)
            return@withContext emptyList()
        }
    }
}
