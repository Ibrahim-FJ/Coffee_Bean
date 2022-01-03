package com.ibrahimf.coffeebean.addProduct.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.ibrahimf.coffeebean.addProduct.util.FirebaseUtils
import com.ibrahimf.coffeebean.network.ProductDataSource
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*


class ProductFireStoreDataSource(
    private val fireBaseDb: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ProductDataSource {

    override suspend fun addProduct(product: Product) {

        uploadImageToFireStore(product.imageUri).collect {
            val productDetails = hashMapOf(
                "title" to product.title,
                "details" to product.details,
                "imageUri" to it,
                "location" to product.location,
                "publisher" to addUserId(),
                "publishDate" to addTimeStamp()
            )

            fireBaseDb.collection("products")
                .add(productDetails)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully written!")

                }
                .addOnFailureListener {
                    println("Error writing document")
                }

        }


    }

    override fun addUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }

    override fun addTimeStamp(): Long {
        return Calendar.getInstance().timeInMillis
    }


    override suspend fun getAllProducts(): Flow<List<Product>> = callbackFlow {

        try {
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


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }

    }


    fun uploadImageToFireStore(imagesList: List<String>): Flow<List<String>> = callbackFlow {

        val storageRef = Firebase.storage.reference
        val scope = async {
            val imageList = mutableListOf<String>()
            for (i in imagesList) {
                val reference = storageRef.child("images/${Calendar.getInstance().timeInMillis}")
                val imageUri = reference.putFile(i.toUri()).continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    reference.downloadUrl
                }.await()
                imageList.add(imageUri.toString())
            }
            return@async imageList
        }
        trySend(scope.await())

        awaitClose { }
    }

}