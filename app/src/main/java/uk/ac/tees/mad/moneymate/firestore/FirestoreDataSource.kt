package uk.ac.tees.mad.moneymate.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }
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
}
