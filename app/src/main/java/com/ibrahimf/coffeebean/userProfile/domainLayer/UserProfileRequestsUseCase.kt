package com.ibrahimf.coffeebean.userProfile.domainLayer

import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository

class UserProfileRequestsUseCase(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getUserReservationRequest()

}