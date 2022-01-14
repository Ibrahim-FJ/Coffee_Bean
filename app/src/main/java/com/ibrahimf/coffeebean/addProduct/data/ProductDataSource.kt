package com.ibrahimf.coffeebean.addProduct.data


import android.net.Uri
import androidx.lifecycle.LiveData
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.userProfile.model.User
import kotlinx.coroutines.flow.Flow


interface ProductDataSource {

    suspend fun addProduct(product: Product)

    suspend fun getAllProducts(): Flow<List<Product>>

    suspend fun addReservation(order: Order): Boolean

    suspend fun getUserOrders(): Flow<List<String>>

    suspend fun getUserReservationRequest(): Flow<List<String>>

    suspend fun getProductsByProductIDForUserOrders(): Flow<List<Product>>

    suspend fun getProductsByProductIDForUserReservationRequest(): Flow<List<Product>>

    suspend fun getUserPosts(): Flow<List<Product>>

    suspend fun editProduct(product: Product)

    suspend fun addUser(user: User)

    suspend fun getUser(): Flow<User>

    suspend fun deletePost(productID: String)




}
