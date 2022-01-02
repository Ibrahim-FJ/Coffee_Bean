package com.ibrahimf.coffeebean.addProduct.data

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ibrahimf.coffeebean.network.ProductDataSource
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.*


class ProductFireStoreDataSource(
    private val fireBaseDb: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductDataSource {

    override suspend fun addProduct(product: Product) {

        val productDetails = hashMapOf(
            "title" to product.title,
            "details" to product.details,
            "location" to product.location,
            "publisher" to addUserId(),
            "publishDate" to addTimeStamp()
        )


        fireBaseDb.collection("products")
            .add(productDetails)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully written!")
                addImageToFirebaseStorage(product, it.id)

            }
            .addOnFailureListener {
                println("Error writing document")
            }

    }

    override fun addUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun addTimeStamp(): Long {
        return Calendar.getInstance().timeInMillis
    }

    override fun addImageToFirebaseStorage(product: Product, documentId: String): Uri? {
        val storageRef = Firebase.storage.reference
        var downloadUri: Uri? = null
        for (i in product.imageUri) {
            val ref = storageRef.child("images/${Calendar.getInstance().timeInMillis}")
            ref.putFile(i.toUri()).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                ref.downloadUrl
            }.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    var imagesUriList: MutableList<Uri> = mutableListOf()
                    imagesUriList.add(task.result)
                    downloadUri = task.result



                    addImage(task.result, documentId)

                } else {
                    // Handle failures
                    // ...
                }
            }

        }

        return downloadUri
    }


    override suspend fun getAllProducts(): Flow<List<Product>> = callbackFlow {

        fireBaseDb.collection("products")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    return@addSnapshotListener
                }

                var list = mutableListOf<Product>()
                snapshot?.documents?.forEach {
                    if (it.exists()) {
                        val productList = it.toObject(Product::class.java)
                        list.add(productList!!)
                        //    Log.d("TAG", "Current data: ${it.data}")
                    } else {
                        //      Log.d("TAG", "Current data: null")
                    }

                }
                trySend(list)

            }

        awaitClose {

        }

    }


    fun addImage(imageUri: Uri, documentId: String) {
        val productDetails = mapOf(
            "imageUri" to listOf(imageUri.toString(), imageUri.toString())
        )

        fireBaseDb.collection("products").document(documentId)
            .update(productDetails)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully written!")

            }
            .addOnFailureListener {
                println("Error writing document")
            }
    }

}