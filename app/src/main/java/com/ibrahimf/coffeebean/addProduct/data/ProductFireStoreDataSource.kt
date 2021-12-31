package com.ibrahimf.coffeebean.addProduct.data

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.ibrahimf.coffeebean.network.ProductDataSource
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.lang.Exception
import java.util.*


class ProductFireStoreDataSource(
    private val fireBaseDb: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :ProductDataSource{

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

            }
            .addOnFailureListener {
                println("Error writing document")
            }



      //  var file = Uri.fromFile(File())



//        product.imageUrl.forEach {
//            riversRef.putFile(it)
//
//        }


//        for (i in product.imageUrl){
//            val riversRef = storageRef.child("images/${Calendar.getInstance().timeInMillis}")
//            riversRef.putFile(i).addOnCompleteListener{
//                if (it.isSuccessful){
//                    println("Url = ${it.result}")
//                }
//            }
//
//
//        }

 }

    override fun addUserId(): String? {
       return Firebase.auth.currentUser?.uid
    }

    override fun addTimeStamp(): Long {
        return Calendar.getInstance().timeInMillis
    }

    override suspend fun addImageToFirebaseStorage(product: Product): Uri? {
        TODO("Not yet implemented")
    }

//    override suspend fun addImageToFirebaseStorage(product: Product): Uri? {
//        val storageRef = Firebase.storage.reference
//        var downloadUri: Uri? = null
//        for (i in product.imageUrl) {
//            val ref = storageRef.child("images/${Calendar.getInstance().timeInMillis}")
//            ref.putFile(i!!).continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    task.exception?.let {
//                        throw it
//                    }
//                }
//                ref.downloadUrl
//            }.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    downloadUri = task.result
//                } else {
//                    // Handle failures
//                    // ...
//                }
//            }.await()
//
//        }
//        println("add image = $downloadUri")
//
//        return downloadUri
//    }


    override suspend fun getAllProducts(): Flow <Product>  = callbackFlow{

        fireBaseDb.collection("products").addSnapshotListener{snapshot, exception->
            if(exception != null){
                return@addSnapshotListener
            }
            snapshot?.documents?.forEach {
                if (it.exists()) {
                    val productList = it.toObject(Product::class.java)
                    trySend(productList!!)
               //    Log.d("TAG", "Current data: ${it.data}")
                } else {
              //      Log.d("TAG", "Current data: null")
                }

            }


        }

        awaitClose {

        }

    }

}