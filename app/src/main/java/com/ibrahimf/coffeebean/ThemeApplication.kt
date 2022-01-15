package com.ibrahimf.coffeebean

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.ibrahimf.coffeebean.userPreferences.ThemeProvider

class ThemeApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val theme = ThemeProvider(this).getThemeFromPreferences()
        AppCompatDelegate.setDefaultNightMode(theme)
    }
}