package uk.ac.tees.mad.moneymate.firestore

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StorageDataSource @Inject constructor() {
    private val storage = FirebaseStorage.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun uploadAttachment(uri: String): String = withContext(Dispatchers.IO) {
        val file = Uri.parse(uri)
        val reference = storage.reference.child("users/${auth.currentUser?.uid}/attachments/${file.lastPathSegment}")
        val uploadTask = reference.putFile(file)
        uploadTask.await()
        return@withContext reference.downloadUrl.await().toString()
    }

    suspend fun deleteAttachment(url: String) = withContext(Dispatchers.IO) {
        val reference = storage.getReferenceFromUrl(url)
        reference.delete().await()
    }
}