package com.ibrahimf.coffeebean.userProfile.uiLayer


enum class LOADING_STATUS { LOADING, DONE, ERROR }

data class UserRegistrationUiState(val loadingStatus: LOADING_STATUS = LOADING_STATUS.DONE, val errorMsg: String = "")


data class UserProfileUiState(val name: String = "", val userLocation: String = "")