package com.ibrahimf.coffeebean.reserveOrder.domainLayer

import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.OrderRepository

class ReserveOrderUseCase(private val orderRepository: OrderRepository) {
    suspend operator fun invoke(order: Order) = orderRepository.addOrder(order)
}