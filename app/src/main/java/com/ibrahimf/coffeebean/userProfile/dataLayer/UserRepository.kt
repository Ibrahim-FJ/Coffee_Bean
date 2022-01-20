package com.ibrahimf.coffeebean.userProfile.dataLayer

import com.ibrahimf.coffeebean.network.product.ProductDataSource
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.network.user.UserDataSource
import com.ibrahimf.coffeebean.userProfile.model.User

class
UserRepository(private val userRemoteDataSource: UserDataSource) {


    suspend fun getProductsByProductIDForUserOrders() = userRemoteDataSource.getProductsByProductIDForUserOrders()

    suspend fun getUserPosts() = userRemoteDataSource.getUserPosts()

    suspend fun addUser(user: User) = userRemoteDataSource.addUser(user)

    suspend fun getUser() = userRemoteDataSource.getUser()

    suspend fun getUserReservationRequest() = userRemoteDataSource.getUserReservationRequest()

    suspend fun getBuyerInformation(buyer: String) = userRemoteDataSource.getBuyerInformation(buyer)

}