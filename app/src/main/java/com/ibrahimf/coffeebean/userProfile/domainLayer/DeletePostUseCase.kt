package com.ibrahimf.coffeebean.userProfile.domainLayer

import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository

class DeletePostUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(productID: String) = userRepository.deletePost(productID)

}