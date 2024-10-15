package uk.ac.tees.mad.moneymate.repo

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.moneymate.mdoels.User
import javax.inject.Inject

class AuthenticationRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {
    // Sign In with Email & Password
    suspend fun signInWithEmailPassword(email: String, password: String): AuthResult? {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            null
        }
    }

    // Sign Up with Email & Password
    suspend fun signUpWithEmailPassword(
        name: String,
        email: String,
        password: String
    ): AuthResult? {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            result?.user?.let {
                saveUserToFirestore(User(uid = it.uid, name = name, email = email))
            }
            result
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Save user information to Firestore
    private suspend fun saveUserToFirestore(user: User) {
        try {
            firestore.collection("users").document(user.uid).set(user).await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    // if user is authenticated
    fun isUserAuthenticated(): Boolean {
        return firebaseAuth.currentUser != null
    }

    // Sign out
    fun signOut() {
        firebaseAuth.signOut()
    }
}
