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
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import java.util.*

class UserFireStoreDataSource(private val fireBaseDb: FirebaseFirestore) : UserDataSource {


    override suspend fun getUser(): Flow<User> = callbackFlow {
        try {
            fireBaseDb.collection("users").document(FirebaseUtils.getUserId()!!)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        return@addSnapshotListener
                    }

                    if (snapshot?.data != null) {
                        val user = snapshot.toObject(User::class.java)
                        trySend(user!!)
                    }

                }


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }


        awaitClose { }
    }


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


    override suspend fun addUser(user: User) {

        uploadUserImageToStorage(user.userImage).collect {
            val productDetails = hashMapOf(
                "userName" to user.userName,
                "userImage" to it,
                "userLocation" to user.userLocation,
                "userPhone" to FirebaseUtils.getUserPhone(),
                "userID" to FirebaseUtils.getUserId(),
            )

            fireBaseDb.collection("users").document(FirebaseUtils.getUserId()!!)
                .set(productDetails)
                .addOnSuccessListener {
                    println("DocumentSnapshot successfully written!")

                }
                .addOnFailureListener {
                    println("Error writing document")
                }

        }


    }


    override suspend fun getUserPosts(): Flow<List<Product>> = callbackFlow {
        try {
            fireBaseDb.collection("products").whereEqualTo("publisher", FirebaseUtils.getUserId())
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


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }

        awaitClose { }

    }


    override suspend fun getUserReservationRequest(): Flow<List<String>> = callbackFlow {

        try {
            val scope = async {
                val reservationRequestList = mutableListOf<String>()

                fireBaseDb.collection("orders").whereEqualTo("seller", FirebaseUtils.getUserId())
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


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }
        awaitClose { }

    }


    override suspend fun getUserOrders(): Flow<List<String>> = callbackFlow {

        try {
            val scope1 = async {
                val ordersList = mutableListOf<String>()

                fireBaseDb.collection("orders").whereEqualTo("buyer", FirebaseUtils.getUserId())
                    .addSnapshotListener { snapshot, exception ->
                        if (exception != null) {
                            return@addSnapshotListener
                        }
                        snapshot?.documents?.forEach {
                            if (it.exists()) {

                                //    Log.e("TAG", "getUserOrders: ${it.data}")

                                val order = it.toObject(Order::class.java)
                                ordersList.add(order?.productID!!)
                                // order?.let { it1 -> ordersList.add(it1.productID) }
                                //Log.e("TAG", "getUserOrders fun : ${ordersList}")

                            }

                        }
                        trySend(ordersList)

                    }

                return@async ordersList
            }
            trySend(scope1.await())


        } catch (exception: Exception) {
            Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

        }
        awaitClose { }


    }


    override suspend fun getProductsByProductIDForUserOrders(): Flow<List<Product>> = callbackFlow {

        getUserOrders().collect { productID ->
            try {
                Log.e("TAG", "getProductsByProductIDForUserOrdersssss: $productID")

                val list = mutableListOf<Product>()

                for (i in productID) {
                    fireBaseDb.collection("products").whereEqualTo("productID", i)
                        .addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                return@addSnapshotListener
                            }


                            snapshot?.documents?.forEach {
                                if (it.exists()) {

                                    val productList = it.toObject(Product::class.java)
                                    list.add(productList!!)
                                    //    Log.d("TAG", "Current data: ${it.data}")
                                } else {
                                    //      Log.d("TAG", "Current data: null")
                                }

                            }
                            Log.e("TAG", "getProductsByProductIDForUserOrdersaaaaaaa: $list")
                            trySend(list)

                        }

                }


            } catch (exception: Exception) {
                Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

            }

        }
        awaitClose {}

    }

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
                                        val productList = it.toObject(Product::class.java)
                                        list.add(productList!!)
                                        //    Log.d("TAG", "Current data: ${it.data}")
                                    } else {
                                        //      Log.d("TAG", "Current data: null")
                                    }

                                }
                                trySend(list)

                            }

                    }

                } catch (exception: Exception) {
                    Log.e("Exception", "getAllProducts: ${exception.message.toString()}")

                }

            }

            awaitClose {

            }
        }


}