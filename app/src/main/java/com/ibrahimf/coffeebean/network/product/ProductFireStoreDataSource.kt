package com.ibrahimf.coffeebean.network.product


import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.userProfile.model.User
import com.ibrahimf.coffeebean.util.FirebaseUtils.getTimeStamp
import com.ibrahimf.coffeebean.util.FirebaseUtils.getUserId
import com.ibrahimf.coffeebean.util.FirebaseUtils.getUserPhone
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*


class ProductFireStoreDataSource(
    private val fireBaseDb: FirebaseFirestore
) : ProductDataSource {

    // function to add products
    override suspend fun addProduct(product: Product) {

        uploadImageToFireStore(product.imageUri).collect {
            val productDetails = hashMapOf(
                "title" to product.title,
                "details" to product.details,
                "imageUri" to it,
                "location" to product.location,
                "publisher" to getUserId(),
                "publishDate" to getTimeStamp()
            )

            fireBaseDb.collection("products")
                .add(productDetails)
                .addOnSuccessListener { documentReference ->
                    setProductDocumentID(documentReference.id)
                    println("DocumentSnapshot successfully written!")

                }
                .addOnFailureListener {
                    println("Error writing document")
                }

        }


    }// end.......

    // function to retrieve all products from firestore database
    override suspend fun getAllProducts(): Flow<List<Product>> = callbackFlow {

        try {
            fireBaseDb.collection("products")
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }

                    val list = mutableListOf<Product>()
                    snapshot?.documents?.forEach {
                        if (it.exists()) {
                            try {
                                val productList = it.toObject(Product::class.java)
                                list.add(productList!!)
                            }catch (e: Exception){

                            }

                        }

                    }
                    trySend(list)

                }


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }

        awaitClose { }

    }// end......


    // function to upload images into firestore
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
    }// end......


    // function to add order
    override suspend fun addReservation(order: Order): Boolean {

        if (order.seller != getUserId()) {
            val orderDetails = hashMapOf(
                "quantity" to order.quantity,
                "message" to order.message,
                "seller" to order.seller,
                "productID" to order.productID,
                "buyer" to getUserId(),
                "timeStamp" to getTimeStamp()
            )

            fireBaseDb.collection("orders")
                .add(orderDetails)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully written!")

                }
                .addOnFailureListener {
                    println("Error writing document")
                }
            return true

        }
        return false
    }// end....

    private fun setProductDocumentID(documentId: String) {
        val orderDetails = mapOf(
            "productID" to documentId
        )

        fireBaseDb.collection("products").document(documentId)
            .update(orderDetails)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully written!")

            }
            .addOnFailureListener {
                println("Error writing document")
            }

    }

    override suspend fun deletePost(productID: String) {
        fireBaseDb.collection("products").document(productID)
            .delete()
            .addOnSuccessListener {
                Log.d("TAG", "DocumentSnapshot successfully deleted!")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error deleting document", e)
            }
    }



}

