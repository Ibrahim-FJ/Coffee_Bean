package com.ibrahimf.coffeebean.network


import com.ibrahimf.coffeebean.network.models.Product


interface ProductDataSource {

    suspend fun addProduct(product: Product)

}
