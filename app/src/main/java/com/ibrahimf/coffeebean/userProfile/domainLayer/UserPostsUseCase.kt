package com.ibrahimf.coffeebean.userProfile.domainLayer

import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository

class UserPostsUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getUserPosts()
}