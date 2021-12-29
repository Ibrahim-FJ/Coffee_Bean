package com.ibrahimf.coffeebean.network


import android.net.Uri
import com.ibrahimf.coffeebean.network.models.Product


interface ProductDataSource {

    suspend fun addProduct(product: Product)
    fun addUserId(): String?
    fun addTimeStamp(): Long

    suspend fun addImageToFirebaseStorage(product: Product): Uri?
    suspend fun getAllProducts(product: Product)



}
