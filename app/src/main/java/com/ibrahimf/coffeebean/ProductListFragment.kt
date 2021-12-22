package com.ibrahimf.coffeebean

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProductListFragment : Fragment() {
    var isSignedIn = false

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    val providers = arrayListOf(AuthUI.IdpConfig.PhoneBuilder().build())

    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.login -> {
                signInLauncher.launch(signInIntent)
            }
            R.id.logout -> {
                signOut()
                isSignedIn = false
            }

        }

        return true
    }


    override fun onPrepareOptionsMenu(menu: Menu) {

        if (isSignedIn) {
            menu.findItem(R.id.login)?.isVisible = false
            menu.findItem(R.id.logout)?.isVisible = true
        } else {
            menu.findItem(R.id.login)?.isVisible = true
            menu.findItem(R.id.logout)?.isVisible = false
        }

    }


    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null).
        if (Firebase.auth.currentUser != null) {
            isSignedIn = true
        }

    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            println(user?.uid)
        } else {
            println("none")
        }
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this.requireContext())

    }

}