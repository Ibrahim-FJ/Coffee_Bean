package com.ibrahimf.coffeebean.addProduct.domainLayer

import com.ibrahimf.coffeebean.addProduct.dataLayer.ProductRepository
import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository

class DeletePostUseCase(private val productRepository: ProductRepository) {

    suspend operator fun invoke(productID: String) = productRepository.deletePost(productID)

}