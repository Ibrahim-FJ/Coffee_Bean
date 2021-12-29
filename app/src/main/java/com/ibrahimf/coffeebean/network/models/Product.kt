package com.ibrahimf.coffeebean.network.models

import android.net.Uri


data class Product(
     val title: String,
     val details: String,
     val imageUrl: List<Uri>,
     val location: Double,
     val publisher: String = "",
     val publishDate: Long = 0
)

