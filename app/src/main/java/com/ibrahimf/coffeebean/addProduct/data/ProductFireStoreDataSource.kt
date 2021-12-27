package com.ibrahimf.coffeebean.addProduct.data

import com.google.firebase.firestore.FirebaseFirestore
import com.ibrahimf.coffeebean.network.ProductDataSource
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


class ProductFireStoreDataSource(
    private val fireBaseDb: FirebaseFirestore,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) :ProductDataSource{

    override suspend fun addProduct(product: Product) {
        val db = fireBaseDb

        db.collection("products").document("ffgg")
            .set(product)
            .addOnSuccessListener {
                println("DocumentSnapshot successfully written!")

            }
            .addOnFailureListener {
                println(it.message)
                println(it.cause)

                println("Error writing document")
            }
    }

}