package com.ibrahimf.coffeebean.network.models



data class Product(
     val title: String = "",
     val details: String = "",
     val imageUri: List<String> = listOf(),
     val location: Double = 0.0,
     val publisher: String = "",
     val publishDate: Long = 0
)

