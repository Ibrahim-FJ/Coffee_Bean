package com.ibrahimf.coffeebean.util

import android.net.Uri
import androidx.core.net.toUri
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.collections.ArrayList

object FirebaseUtils {

    // function to get the userId from firebase authentication.
    fun getUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }// end.......

    fun getUserPhone(): String? {
        return Firebase.auth.currentUser?.phoneNumber
    }// end.......


    // function to get the current time
    fun getTimeStamp(): Long {
        return Calendar.getInstance().timeInMillis
    }// end.......

}