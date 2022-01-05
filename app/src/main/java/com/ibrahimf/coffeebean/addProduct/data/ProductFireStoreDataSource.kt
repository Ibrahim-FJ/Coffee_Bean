package com.ibrahimf.coffeebean.addProduct.data

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.*


class ProductFireStoreDataSource(
    private val fireBaseDb: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
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

    // function to get the userId from firebase authentication.
    override fun getUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }// end.......

    // function to get the current time
    override fun getTimeStamp(): Long {
        return Calendar.getInstance().timeInMillis
    }// end.......

    // function to retrieve all products from firestore database
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


    override suspend fun addReservation(order: Order) {

        val orderDetails = hashMapOf(
            "quantity" to order.orderQuantity,
            "message" to order.orderMessage,
            "seller" to order.sellerId,
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

    }

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


    override suspend fun getUserOrders(): Flow<List<String>> = callbackFlow {

        try {
            val scope1 = async {
                val ordersList = mutableListOf<String>()

                fireBaseDb.collection("orders").whereEqualTo("buyer", getUserId())
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            return@addSnapshotListener
                        }
                        snapshot?.documents?.forEach {
                            if (it.exists()) {

                                //  Log.e("TAG", "getUserOrders: ${it.data}")

                                val order = it.toObject(Order::class.java)
                                order?.let { it1 -> ordersList.add(it1.productID) }
                                //Log.e("TAG", "getUserOrders fun : ${ordersList}")
                                trySend(ordersList)

                            }

                        }

                    }

                return@async ordersList
            }
            trySend(scope1.await())

            awaitClose { }

        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }
        awaitClose { }

    }


    override suspend fun getUserReservationRequest(): Flow<List<String>> = callbackFlow {

        try {
            val scope = async {
                val reservationRequestList = mutableListOf<String>()

                fireBaseDb.collection("orders").whereEqualTo("seller", getUserId())
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            return@addSnapshotListener
                        }
                        snapshot?.documents?.forEach {
                            if (it.exists()) {

                                //  Log.e("TAG", "getUserOrders: ${it.data}")

                                val order = it.toObject(Order::class.java)
                                order?.let { it1 -> reservationRequestList.add(it1.productID) }
                                //Log.e("TAG", "getUserOrders fun : ${ordersList}")
                                trySend(reservationRequestList)

                            }

                        }

                    }

                return@async reservationRequestList
            }
            trySend(scope.await())

            awaitClose { }

        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }
        awaitClose { }

    }

    override suspend fun getProductsByProductIDForUserOrders(): Flow<List<Product>> = callbackFlow {

        getUserOrders().collect { productID ->
            try {

                for (i in productID) {
                    fireBaseDb.collection("products").whereEqualTo("productID", i)
                        .addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                return@addSnapshotListener
                            }

                            val list = mutableListOf<Product>()
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

            } catch (exception: Exception) {
                Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

            }

        }

    }

    override suspend fun getProductsByProductIDForUserReservationRequest(): Flow<List<Product>> = callbackFlow{
        getUserReservationRequest().collect { productID ->
            try {

                for (i in productID) {
                    fireBaseDb.collection("products").whereEqualTo("productID", i)
                        .addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                return@addSnapshotListener
                            }

                            val list = mutableListOf<Product>()
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

            } catch (exception: Exception) {
                Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

            }

        }


    }

    override suspend fun getUserPosts(): Flow<List<Product>> = callbackFlow{
        try {
            fireBaseDb.collection("products").whereEqualTo("publisher", getUserId())
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }


                    val list = mutableListOf<Product>()
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


}

