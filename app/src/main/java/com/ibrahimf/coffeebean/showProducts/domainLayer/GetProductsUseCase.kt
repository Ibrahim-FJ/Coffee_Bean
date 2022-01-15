package com.ibrahimf.coffeebean.showProducts.domainLayer

import com.ibrahimf.coffeebean.addProduct.dataLayer.ProductRepository
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val productRepository: ProductRepository){


    suspend operator fun invoke(): Flow<List<Product>> = productRepository.getAllProducts()

}