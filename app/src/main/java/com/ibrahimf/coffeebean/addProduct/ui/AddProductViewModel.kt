package com.ibrahimf.coffeebean.addProduct.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.ibrahimf.coffeebean.addProduct.domain.AddProductUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideAddProductUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideAddUserUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideDeletePostUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideEditProductUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideGetProductsUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideGetUserUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideReserveOrderUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideUserPostsUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideUserProfileOrdersUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideUserProfileRequestUseCase
import com.ibrahimf.coffeebean.camera.PhoneImage
import com.ibrahimf.coffeebean.network.models.Product
import com.ibrahimf.coffeebean.reserveOrder.uiLayer.ReserveOrderViewModel
import com.ibrahimf.coffeebean.showProducts.uiLayer.ProductsListViewModel
import com.ibrahimf.coffeebean.userProfile.uiLayer.UserProfileViewModel
import kotlinx.coroutines.launch


class AddProductViewModel(private val addProductUseCase: AddProductUseCase) : ViewModel() {


    var allImages = MutableLiveData<MutableList<PhoneImage>>()
    var allSelectedImages = MutableLiveData(mutableListOf(PhoneImage("")))



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

}


class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddProductViewModel(provideAddProductUseCase()) as T
        }

        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ProductsListViewModel::class.java))
            return ProductsListViewModel(provideGetProductsUseCase()) as T

        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(ReserveOrderViewModel::class.java))
            return ReserveOrderViewModel(provideReserveOrderUseCase()) as T

        @Suppress("UNCHECKED_CAST")
        if (modelClass.isAssignableFrom(UserProfileViewModel::class.java))
            return UserProfileViewModel(
                provideUserProfileOrdersUseCase(),
                provideUserProfileRequestUseCase(),
                provideUserPostsUseCase(),
                provideEditProductUseCase(),
                provideAddUserUseCase(),
                provideGetUserUseCase(),
                provideDeletePostUseCase()
            ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }


}