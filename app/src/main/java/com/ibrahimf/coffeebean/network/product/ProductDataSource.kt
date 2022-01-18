package com.ibrahimf.coffeebean.network.product


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

    suspend fun deletePost(productID: String)




}
