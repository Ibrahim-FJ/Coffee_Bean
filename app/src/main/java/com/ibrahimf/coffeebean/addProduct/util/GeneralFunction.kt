package com.ibrahimf.coffeebean.addProduct.util

import java.text.SimpleDateFormat
import java.util.*


fun convertMilliSecondsToDate(dateMilliseconds: Long): String{
    val format = SimpleDateFormat("d MMM HH:mm", Locale.getDefault())
    return format.format(Date(dateMilliseconds))
}