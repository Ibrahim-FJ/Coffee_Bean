package com.ibrahimf.coffeebean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ibrahimf.coffeebean.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import androidx.navigation.ui.setupWithNavController


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentsContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding?.bottomNavigationBar?.setupWithNavController(navController)


        navController.addOnDestinationChangedListener{_, destination, _ ->

            if (destination.id == R.id.userRegistrationFragment || destination.id == R.id.phoneImagesFragment){
                binding?.linearLayout?.visibility = View.GONE
            }else{
                binding?.linearLayout?.visibility = View.VISIBLE
            }

        }


    }

}