package com.ibrahimf.coffeebean.userProfile.domainLayer

import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository

class GetBuyerInformationUserCase(private val userRepository: UserRepository) {

    suspend operator fun invoke(buyer: String) = userRepository.getBuyerInformation(buyer)
}
