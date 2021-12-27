package com.ibrahimf.coffeebean.network

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.network.models.Product


interface ProductDataSource {

    suspend fun addProduct(product: Product)

}
