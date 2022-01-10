package com.ibrahimf.coffeebean.userProfile.dataLayer

import com.ibrahimf.coffeebean.addProduct.data.ProductDataSource
import com.ibrahimf.coffeebean.addProduct.data.ProductFireStoreDataSource
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.userProfile.model.User

class
UserRepository(private val userRemoteDataSource: ProductDataSource) {

    suspend fun getProductsByProductIDForUserOrders() = userRemoteDataSource.getProductsByProductIDForUserOrders()

    suspend fun getProductsByProductIDForUserReservationRequest() = userRemoteDataSource.getProductsByProductIDForUserReservationRequest()

    suspend fun getUserPosts() = userRemoteDataSource.getUserPosts()

    suspend fun editProduct(product: Product) = userRemoteDataSource.editProduct(product)

    suspend fun addUser(user: User) = userRemoteDataSource.addUser(user)

    suspend fun getUser() = userRemoteDataSource.getUser()

    suspend fun deletePost(productID: String) = userRemoteDataSource.deletePost(productID)


}