package com.ibrahimf.coffeebean.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ibrahimf.coffeebean.addProduct.uiLayer.ProductViewModel
import com.ibrahimf.coffeebean.reserveOrder.uiLayer.ReserveOrderViewModel
import com.ibrahimf.coffeebean.showProducts.uiLayer.ProductsListViewModel
import com.ibrahimf.coffeebean.userProfile.uiLayer.UserProfileViewModel
import com.ibrahimf.coffeebean.util.ServiceLocator.provideAddProductUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideAddUserUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideDeletePostUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideGetBuyerInformationUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideGetProductsUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideGetUserUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideReserveOrderUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideUserPostsUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideUserProfileOrdersUseCase
import com.ibrahimf.coffeebean.util.ServiceLocator.provideUserProfileRequestUseCase

class ViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(
                provideAddProductUseCase(),
                provideDeletePostUseCase()
            ) as T
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
                provideAddUserUseCase(),
                provideGetUserUseCase(),
                provideGetBuyerInformationUseCase()

                ) as T

        throw IllegalArgumentException("Unknown ViewModel class")
    }


}