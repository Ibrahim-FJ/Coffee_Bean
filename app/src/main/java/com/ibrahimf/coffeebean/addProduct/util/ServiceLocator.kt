package com.ibrahimf.coffeebean.addProduct.util

import com.google.firebase.firestore.FirebaseFirestore
import com.ibrahimf.coffeebean.addProduct.data.ProductFireStoreDataSource
import com.ibrahimf.coffeebean.addProduct.data.ProductRepository
import com.ibrahimf.coffeebean.addProduct.domain.AddProductUseCase
import com.ibrahimf.coffeebean.network.ProductDataSource


object ServiceLocator {

    fun provideProductRemoteDataSource(): ProductDataSource = ProductFireStoreDataSource(
        FirebaseFirestore.getInstance()
    )

    fun provideAppProductRepository(): ProductRepository =
        ProductRepository(provideProductRemoteDataSource())

    fun provideAddProductUseCase(): AddProductUseCase =
        AddProductUseCase(provideAppProductRepository())


}
