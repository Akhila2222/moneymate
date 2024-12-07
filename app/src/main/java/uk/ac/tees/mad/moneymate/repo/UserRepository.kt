package uk.ac.tees.mad.moneymate.repo

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import uk.ac.tees.mad.moneymate.firestore.StorageDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storageDataSource: StorageDataSource
) {

    fun getUserProfile(): Flow<UserProfile> = flow {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val document = firestore.collection("users").document(uid!!).get().await()
        val data = document.data
        val userProfile = UserProfile(
            name = data?.get("name") as String,
            email = data["email"] as String,
            imageUri = data["imageUri"] as String?
        )
        emit(userProfile)
    }

    suspend fun updateUserProfile(userProfile: UserProfile, onSuccess: () -> Unit) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (userProfile.imageUri != null) {
            val url = updateUserImage(uri = Uri.parse(userProfile.imageUri))
            userProfile.imageUri = url
        }
        firestore.collection("users").document(uid!!).set(userProfile).addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener {
            it.printStackTrace()
        }
    }

    suspend fun updateUserImage(uri: Uri): String {
        return storageDataSource.uploadAttachment(uri.toString())
    }
}

data class UserProfile(
    val name: String = "",
    val email: String = "",
    var imageUri: String? = null
)
