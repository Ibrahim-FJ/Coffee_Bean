package com.ibrahimf.coffeebean

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.ibrahimf.coffeebean.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.ui.setupWithNavController
import com.ibrahimf.coffeebean.addProduct.ui.AddProductFragment
import com.ibrahimf.coffeebean.showProducts.uiLayer.ProductListFragment
import com.ibrahimf.coffeebean.userProfile.uiLayer.UserProfileFragment


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

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if (destination.id == R.id.userRegistrationFragment || destination.id == R.id.phoneImagesFragment){
                binding?.bottomNavigationBar?.visibility = View.GONE
            }else{
                binding?.bottomNavigationBar?.visibility = View.VISIBLE
            }

        }

        binding?.bottomNavigationBar?.setupWithNavController(navController)




//        setupActionBarWithNavController(navController)
//
//
//       setCurrentFragment(ProductListFragment())
//
//        binding.apply {
//            add_post_fab?.setOnClickListener {
//                setCurrentFragment(AddProductFragment())
//            }
//
//            profile_item.setOnClickListener {
//                setCurrentFragment(UserProfileFragment())
//
//            }
//
//            home_item.setOnClickListener {
//                setCurrentFragment(ProductListFragment())
//
//            }
//
//        }

    }

//    private fun setCurrentFragment(fragment:Fragment)=
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.fragmentsContainerView,fragment)
//            commit()
//        }


//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
}