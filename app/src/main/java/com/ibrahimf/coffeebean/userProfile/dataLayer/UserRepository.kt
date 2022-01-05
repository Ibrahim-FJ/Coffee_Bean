package com.ibrahimf.coffeebean.userProfile.dataLayer

import com.ibrahimf.coffeebean.addProduct.data.ProductDataSource
import com.ibrahimf.coffeebean.addProduct.data.ProductFireStoreDataSource

class UserRepository(private val userRemoteDataSource: ProductDataSource) {

    suspend fun getProductsByProductIDForUserOrders() = userRemoteDataSource.getProductsByProductIDForUserOrders()

    suspend fun getProductsByProductIDForUserReservationRequest() = userRemoteDataSource.getProductsByProductIDForUserReservationRequest()

    suspend fun getUserPosts() = userRemoteDataSource.getUserPosts()



}