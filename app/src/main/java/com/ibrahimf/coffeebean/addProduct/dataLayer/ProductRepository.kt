package com.ibrahimf.coffeebean.addProduct.dataLayer

import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.network.product.ProductDataSource


class ProductRepository (private val productRemoteDataSource: ProductDataSource) {
    suspend fun addProduct(product: Product) = productRemoteDataSource.addProduct(product)
    suspend fun getAllProducts() = productRemoteDataSource.getAllProducts()
    suspend fun deletePost(productID: String) = productRemoteDataSource.deletePost(productID)

}