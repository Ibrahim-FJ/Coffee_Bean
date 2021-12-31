package com.ibrahimf.coffeebean.network


import android.net.Uri
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.flow.Flow


interface ProductDataSource {

    suspend fun addProduct(product: Product)
    fun addUserId(): String?
    fun addTimeStamp(): Long

    suspend fun addImageToFirebaseStorage(product: Product): Uri?
    suspend fun getAllProducts(): Flow <Product>



}
