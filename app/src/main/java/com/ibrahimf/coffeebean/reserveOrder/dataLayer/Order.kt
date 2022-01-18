package com.ibrahimf.coffeebean.reserveOrder.dataLayer

data class Order(
    val productID: String = "",
    val quantity: String = "",
    val message: String = "",
    val seller: String = "",
    val buyer: String = ""
)
