package com.ibrahimf.coffeebean.network


import android.net.Uri
import androidx.lifecycle.LiveData
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.flow.Flow


interface ProductDataSource {

    suspend fun addProduct(product: Product)
    fun getUserId(): String?
    fun getTimeStamp(): Long

   // fun addImageToFirebaseStorage(product: Product, documentId: String): Uri?
    suspend fun getAllProducts(): Flow<List<Product>>




}
