package com.ibrahimf.coffeebean.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ibrahimf.coffeebean.data.PhoneImage

class ImagesViewModel: ViewModel() {
    var allImages = MutableLiveData<MutableList<PhoneImage>>()
    var allSelectedImages = MutableLiveData<MutableList<PhoneImage>>()


    fun setAllImages(phoneImage: PhoneImage){
        println(allImages.value)
        allImages.value?.add(phoneImage)
    }

    fun getAllImages(): MutableList<PhoneImage>?{
        return allImages.value
    }
}