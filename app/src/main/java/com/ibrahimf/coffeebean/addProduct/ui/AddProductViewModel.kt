package com.ibrahimf.coffeebean.addProduct.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.addProduct.domain.AddProductUseCase
import com.ibrahimf.coffeebean.addProduct.util.ServiceLocator.provideAddProductUseCase
import com.ibrahimf.coffeebean.userData.PhoneImage
import com.ibrahimf.coffeebean.network.models.Product
import kotlinx.coroutines.launch


class AddProductViewModel(private val addProductUseCase: AddProductUseCase): ViewModel() {


    var allImages = MutableLiveData<MutableList<PhoneImage>>()
    var allSelectedImages = MutableLiveData<MutableList<PhoneImage>>()

    fun addProduct(product: Product){
        viewModelScope.launch {
            addProductUseCase.invoke(product)
        }
    }
}

class ViewModelFactory: ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProductViewModel :: class.java)){
            @Suppress("UNCHECKED_CAST")
            return AddProductViewModel(provideAddProductUseCase()) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}