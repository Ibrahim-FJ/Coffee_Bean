package com.ibrahimf.coffeebean.util

import com.google.firebase.firestore.FirebaseFirestore
import com.ibrahimf.coffeebean.addProduct.data.ProductFireStoreDataSource
import com.ibrahimf.coffeebean.addProduct.data.ProductRepository
import com.ibrahimf.coffeebean.addProduct.domain.AddProductUseCase
import com.ibrahimf.coffeebean.showProducts.domainLayer.GetProductsUseCase
import com.ibrahimf.coffeebean.addProduct.data.ProductDataSource
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.OrderRepository
import com.ibrahimf.coffeebean.reserveOrder.domainLayer.ReserveOrderUseCase
import com.ibrahimf.coffeebean.userProfile.dataLayer.UserRepository
import com.ibrahimf.coffeebean.userProfile.domainLayer.*


object ServiceLocator {

    fun provideProductRemoteDataSource(): ProductDataSource = ProductFireStoreDataSource(
        FirebaseFirestore.getInstance()
    )

    fun provideAppProductRepository(): ProductRepository =
        ProductRepository(provideProductRemoteDataSource())

    fun provideAddProductUseCase(): AddProductUseCase =
        AddProductUseCase(provideAppProductRepository())

    fun provideGetProductsUseCase(): GetProductsUseCase = GetProductsUseCase(
        provideAppProductRepository()
    )

    fun provideOrderRepository(): OrderRepository =
        OrderRepository(provideProductRemoteDataSource())

    fun provideReserveOrderUseCase(): ReserveOrderUseCase =
        ReserveOrderUseCase(provideOrderRepository())

    fun provideUserRepository(): UserRepository = UserRepository(provideProductRemoteDataSource())

    fun provideUserProfileOrdersUseCase(): UserProfileOrdersUseCase = UserProfileOrdersUseCase(provideUserRepository())

    fun provideUserProfileRequestUseCase(): UserProfileRequestsUseCase = UserProfileRequestsUseCase(
        provideUserRepository())

    fun provideUserPostsUseCase(): UserPostsUseCase = UserPostsUseCase(provideUserRepository())

    fun provideEditProductUseCase(): EditProductUseCase = EditProductUseCase(provideUserRepository())
    fun provideAddUserUseCase(): AddUserUserCase = AddUserUserCase(provideUserRepository())

    fun provideGetUserUseCase(): GetUserUseCase = GetUserUseCase(provideUserRepository())

    fun provideDeletePostUseCase(): DeletePostUseCase = DeletePostUseCase(provideUserRepository())

}