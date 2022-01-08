package com.ibrahimf.coffeebean.userProfile.domainLayer

import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository
import com.ibrahimf.coffeebean.userProfile.model.User

class AddUserUserCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(user: User) = userRepository.addUser(user)
}