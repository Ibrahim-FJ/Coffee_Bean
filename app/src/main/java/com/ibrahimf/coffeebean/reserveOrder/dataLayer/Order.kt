package com.ibrahimf.coffeebean.reserveOrder.dataLayer

data class Order(
    val productID: String = "",
    val orderQuantity: String = "",
    val orderMessage: String = "",
    val sellerId: String = ""
)
