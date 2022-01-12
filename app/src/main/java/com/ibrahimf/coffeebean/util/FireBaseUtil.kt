package com.ibrahimf.coffeebean.util

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

object FirebaseUtils {

    suspend fun uploadPhotos(photosUri: List<String>): List<String> {
        val storageRef = Firebase.storage.reference
        val photosUrls = ArrayList<String>()
        val uploadedPhotosUriLink = withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            (photosUri.indices).map { index ->
                async(Dispatchers.IO) {
                    uploadPhoto(storageRef, photosUri[index])
                }
            }
        }.awaitAll()

        uploadedPhotosUriLink.forEach { photoUriLink -> photosUrls.add(photoUriLink.toString()) }
        return photosUrls
    }


    private suspend fun uploadPhoto(storageRef: StorageReference, photoFile: String): Uri {
        val fileName = UUID.randomUUID().toString()
     //   val fileUri = Uri.fromFile(photoFile)

        return storageRef.child(fileName)
            .putFile(photoFile.toUri())
            .await()
            .storage
            .downloadUrl
            .await()
    }
}