package com.ibrahimf.coffeebean.network.user

import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.userProfile.model.User
import kotlinx.coroutines.flow.Flow

interface UserDataSource {


    suspend fun getUser(): Flow<User>

    suspend fun getUserPosts(): Flow<List<Product>>


    suspend fun getUserOrders(): Flow<List<String>>

    suspend fun getUserReservationRequest(): Flow<List<Order>>

    suspend fun addUser(user: User)

    suspend fun getProductsByProductIDForUserOrders(): Flow<List<Product>>

    suspend fun getProductsByProductIDForUserReservationRequest(): Flow<List<Product>>

    suspend fun getBuyerInformation(buyer: String): Flow<User>

}