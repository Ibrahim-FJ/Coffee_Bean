package com.ibrahimf.coffeebean.userProfile.domainLayer

import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository

class EditProductUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(product: Product) = userRepository.editProduct(product)
}