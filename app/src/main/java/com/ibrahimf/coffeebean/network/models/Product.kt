package com.ibrahimf.coffeebean.network.models

import com.google.firebase.firestore.GeoPoint


data class Product(
     val productID: String = "",
     val title: String = "",
     val details: String = "",
     val imageUri: List<String> = listOf(),
     val location : GeoPoint = GeoPoint(0.0, 0.0),
     val publisher: String = "",
     val publishDate: Long = 0
)

