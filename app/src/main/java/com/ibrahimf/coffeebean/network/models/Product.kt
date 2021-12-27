package com.ibrahimf.coffeebean.network.models

import android.net.Uri

data class Product(
     val title: String,
     val details: String,
     val imageUrl: String,
     val location: Double,
     val publisher: String,
     val publishDate: Long
)
