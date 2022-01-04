package com.ibrahimf.coffeebean.addProduct.data

import com.ibrahimf.coffeebean.network.models.Product



class ProductRepository (private val productRemoteDataSource: ProductDataSource) {
    suspend fun addProduct(product: Product) = productRemoteDataSource.addProduct(product)
    suspend fun getAllProducts() = productRemoteDataSource.getAllProducts()

}