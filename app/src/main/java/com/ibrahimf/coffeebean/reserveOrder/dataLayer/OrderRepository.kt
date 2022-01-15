package com.ibrahimf.coffeebean.reserveOrder.dataLayer

import com.ibrahimf.coffeebean.network.product.ProductDataSource

class OrderRepository(private val productRemoteDataSource: ProductDataSource) {

    suspend fun addOrder(order: Order) = productRemoteDataSource.addReservation(order)

}