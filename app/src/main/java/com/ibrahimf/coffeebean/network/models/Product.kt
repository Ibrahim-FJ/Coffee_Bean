package com.ibrahimf.coffeebean.network.models



data class Product(
     val title: String = "",
     val details: String = "",
     val imageUri: List<String> = listOf("https://firebasestorage.googleapis.com/v0/b/coffee-bean-b8eeb.appspot.com/o/images%2F1641107604454?alt=media&token=9517252b-c8df-45aa-88b0-a2ef2fef8757"),
     val location: Double = 0.0,
     val publisher: String = "",
     val publishDate: Long = 0
)

