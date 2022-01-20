package com.ibrahimf.coffeebean.network.user

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.userProfile.model.User
import com.ibrahimf.coffeebean.util.FirebaseUtils
import com.ibrahimf.coffeebean.util.FirebaseUtils.getUserId
import com.ibrahimf.coffeebean.util.FirebaseUtils.getUserPhone
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.Exception

class UserFireStoreDataSource(private val fireBaseDb: FirebaseFirestore) : UserDataSource {


    // function to get user from firebase
    override suspend fun getUser(): Flow<User> = callbackFlow {
        try {
            fireBaseDb.collection("users").document(FirebaseUtils.getUserId()!!)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot?.data != null) {
                        try {
                            val user = snapshot.toObject(User::class.java)
                            trySend(user!!)
                        } catch (e: Exception) {

                        }

                    }

                }


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }

        awaitClose { }
    }// end.....


    // function to upload the user image to the firebase
    fun uploadUserImageToStorage(userImage: String): Flow<String> = callbackFlow {

        val storageRef = Firebase.storage.reference
        val scope = async {
            val reference =
                storageRef.child("usersProfileImages/${Calendar.getInstance().timeInMillis}")
            val imageUri = reference.putFile(userImage.toUri()).continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                reference.downloadUrl
            }.await()


            return@async imageUri.toString()
        }
        trySend(scope.await())

        awaitClose { }
    }// end......


    // function to add user to the firebase
    override suspend fun addUser(user: User) {

        uploadUserImageToStorage(user.userImage).collect {
            val userDetails = hashMapOf(
                "userName" to user.userName,
                "userImage" to it,
                "userLocation" to user.userLocation,
                "userPhone" to getUserPhone(), // get user phone to add
                "userID" to getUserId(), // get user id to add
            )

            fireBaseDb.collection("users").document(getUserId()!!)
                .set(userDetails)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully written!")

                }
                .addOnFailureListener {
                    println("Error writing document")
                }

        }


    }// end.....


    // function to retrieve all user posts
    override suspend fun getUserPosts(): Flow<List<Product>> = callbackFlow {
        try {
            fireBaseDb.collection("products").whereEqualTo("publisher", getUserId())
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
                            } catch (e: Exception) {
                                Log.e("Exception", "getUserPosts: ${e.message.toString()}")
                            }

                        }

                    }
                    trySend(list)

                }


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }

        awaitClose { }

    }// end.......


    // function to get all user reservation request
    override suspend fun getUserReservationRequest(): Flow<List<Order>> = callbackFlow {

        try {

            val reservationRequestList = mutableListOf<Order>()

            fireBaseDb.collection("orders").whereEqualTo("seller", FirebaseUtils.getUserId())
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }
                    snapshot?.documents?.forEach {
                        if (it.exists()) {

                            try {
                                val order = it.toObject(Order::class.java)
                                order?.let { it1 -> reservationRequestList.add(it1) }
                            } catch (e: Exception) {
                                Log.e(
                                    "Exception",
                                    "getUserReservationRequest: ${e.message.toString()}"
                                )

                            }

                        }

                    }

                    trySend(reservationRequestList)
                }

        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }
        awaitClose { }

    }// end........


    // function to get buyer information
    override suspend fun getBuyerInformation(buyer: String): Flow<User> = callbackFlow {
        try {
            fireBaseDb.collection("users").document(buyer)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot?.data != null) {
                        try {
                            val user = snapshot.toObject(User::class.java)
                            trySend(user!!)
                        } catch (e: Exception) {
                            Log.e("Exception", "getBuyerInformation: ${e.message.toString()}")
                        }

                    }

                }


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }

        awaitClose { }
    }// end......


    // function to get user orders
    override suspend fun getUserOrders(): Flow<List<String>> = callbackFlow {

        try {
            val ordersList = mutableListOf<String>()

            fireBaseDb.collection("orders").whereEqualTo("buyer", getUserId())
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }
                    snapshot?.documents?.forEach {
                        if (it.exists()) {
                            try {
                                val order = it.toObject(Order::class.java)
                                ordersList.add(order?.productID!!)
                            } catch (e: Exception) {
                                Log.e("Exception", "getUserOrders: ${e.message.toString()}")

                            }

                        }

                    }
                    trySend(ordersList)

                }

        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }
        awaitClose { }


    } // end......


    // function to get products by productID for user orders
    override suspend fun getProductsByProductIDForUserOrders(): Flow<List<Product>> = callbackFlow {

        getUserOrders().collect { productID ->
            try {

                val list = mutableListOf<Product>()

                for (i in productID) {
                    fireBaseDb.collection("products").whereEqualTo("productID", i)
                        .addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                return@addSnapshotListener
                            }

                            snapshot?.documents?.forEach {
                                if (it.exists()) {

                                    try {
                                        val productList = it.toObject(Product::class.java)
                                        list.add(productList!!)
                                    } catch (e: Exception) {
                                        Log.e(
                                            "Exception",
                                            "getProductsByProductIDForUserOrders: ${e.message.toString()}"
                                        )
                                    }

                                }

                            }
                            trySend(list)

                        }
                }


            } catch (exception: Exception) {
                Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

            }

        }
        awaitClose {}

    }// end.......

    // function to get products by ProductID for user reservation request
    override suspend fun getProductsByProductIDForUserReservationRequest(): Flow<List<Product>> =
        callbackFlow {
            getUserReservationRequest().collect { productID ->
                try {
                    val list = mutableListOf<Product>()
                    for (i in productID) {
                        fireBaseDb.collection("products").whereEqualTo("productID", i)
                            .addSnapshotListener { snapshot, exception ->
                                if (exception != null) {
                                    return@addSnapshotListener
                                }

                                snapshot?.documents?.forEach {
                                    if (it.exists()) {

                                        try {
                                            val productList = it.toObject(Product::class.java)
                                            list.add(productList!!)
                                        } catch (e: Exception) {
                                            Log.e(
                                                "Exception",
                                                "getProductsByProductIDForUserReservationRequest: ${e.message.toString()}"
                                            )

                                        }

                                    }

                                }
                                trySend(list)

                            }

                    }

                } catch (exception: Exception) {
                    Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

                }

            }

            awaitClose {}
        } // end......

}