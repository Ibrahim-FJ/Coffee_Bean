package com.ibrahimf.coffeebean.addProduct.data


import android.net.Uri
import androidx.lifecycle.LiveData
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import kotlinx.coroutines.flow.Flow


interface ProductDataSource {

    suspend fun addProduct(product: Product)
    fun getUserId(): String?
    fun getTimeStamp(): Long

   // fun addImageToFirebaseStorage(product: Product, documentId: String): Uri?
    suspend fun getAllProducts(): Flow<List<Product>>

    suspend fun addReservation(order: Order)




}