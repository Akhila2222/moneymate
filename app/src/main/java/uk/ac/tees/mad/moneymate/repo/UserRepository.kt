package uk.ac.tees.mad.moneymate.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.moneymate.firestore.StorageDataSource

class UserRepository(
    private val firestore: FirebaseFirestore,
    private val storageDataSource: StorageDataSource
) {

    fun getUserProfile(): Flow<UserProfile> = flow {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val document = firestore.collection("users").document(uid!!).get().await()
        val userProfile = document.toObject(UserProfile::class.java) ?: UserProfile()
        emit(userProfile)
    }

    fun updateUserProfile(userProfile: UserProfile) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore.collection("users").document(uid!!).set(userProfile)
    }

    suspend fun updateUserImage(uri: Uri): String {
       return storageDataSource.uploadAttachment(uri.toString())
    }
}
