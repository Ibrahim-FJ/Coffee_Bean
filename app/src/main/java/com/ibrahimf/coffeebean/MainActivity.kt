package com.ibrahimf.coffeebean

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.ibrahimf.coffeebean.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var binding: ActivityMainBinding? = null
    private var signIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentsContainerView) as NavHostFragment
        navController = navHostFragment.navController

        binding?.bottomNavigationBar?.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { _, destination, _ ->

            if (destination.id == R.id.userRegistrationFragment || destination.id == R.id.phoneImagesFragment) {
                binding?.linearLayout?.visibility = View.GONE
            } else {
                binding?.linearLayout?.visibility = View.VISIBLE
            }

        }

        binding?.bottomNavigationBar?.menu?.findItem(R.id.addProductFragment)
            ?.setOnMenuItemClickListener {
                if (Firebase.auth.currentUser == null){
                    Toast.makeText(this, "please sign in", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.firebaseRegistrationFragment)
                    signIn = true
                }else if (Firebase.auth.currentUser != null){
                    signIn = false

                }
                signIn
            }

        binding?.bottomNavigationBar?.menu?.findItem(R.id.userProfileFragment)
            ?.setOnMenuItemClickListener {
                if (Firebase.auth.currentUser == null){
                    Toast.makeText(this, "please sign in", Toast.LENGTH_SHORT).show()
                    navController.navigate(R.id.firebaseRegistrationFragment)
                    signIn = true
                } else{
                    signIn = false
                }
                signIn
            }


    }

}