package com.ibrahimf.coffeebean.addProduct.domain

import com.ibrahimf.coffeebean.addProduct.data.ProductRepository
import com.ibrahimf.coffeebean.network.models.Product

class GetProductsUseCase(private val productRepository: ProductRepository){


    suspend operator fun invoke() = productRepository.getAllProducts()

}