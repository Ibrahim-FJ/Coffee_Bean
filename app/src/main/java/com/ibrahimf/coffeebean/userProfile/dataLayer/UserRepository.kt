package com.ibrahimf.coffeebean.userProfile.dataLayer

import com.ibrahimf.coffeebean.addProduct.data.ProductDataSource
import com.ibrahimf.coffeebean.addProduct.data.ProductFireStoreDataSource
import com.ibrahimf.coffeebean.network.models.Product

class UserRepository(private val userRemoteDataSource: ProductDataSource) {

    suspend fun getProductsByProductIDForUserOrders() = userRemoteDataSource.getProductsByProductIDForUserOrders()

    suspend fun getProductsByProductIDForUserReservationRequest() = userRemoteDataSource.getProductsByProductIDForUserReservationRequest()

    suspend fun getUserPosts() = userRemoteDataSource.getUserPosts()

    suspend fun editProduct(product: Product) = userRemoteDataSource.editProduct(product)



}