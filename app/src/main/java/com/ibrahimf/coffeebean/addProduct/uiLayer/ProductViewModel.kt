package com.ibrahimf.coffeebean.addProduct.uiLayer

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.addProduct.domainLayer.AddProductUseCase
import com.ibrahimf.coffeebean.addProduct.domainLayer.DeletePostUseCase
import com.ibrahimf.coffeebean.camera.PhoneImage
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.launch


class ProductViewModel(private val addProductUseCase: AddProductUseCase, private val deletePostUseCase: DeletePostUseCase) : ViewModel() {


    var allImages = MutableLiveData<MutableList<PhoneImage>>()
    var allSelectedImages = MutableLiveData<MutableList<PhoneImage>>()
  //  var allSelectedImages = MutableLiveData(mutableListOf(PhoneImage()))



    fun addProduct(product: Product) {
        viewModelScope.launch {
            addProductUseCase.invoke(product)
        }
    }

//    fun addImage(){
//        viewModelScope.launch {
//           // addImageToFirebaseStorageUseCase.invoke()
//        }
//    }


    fun deletePost(productID: String){
        viewModelScope.launch {
            deletePostUseCase.invoke(productID)
        }
    }

}
