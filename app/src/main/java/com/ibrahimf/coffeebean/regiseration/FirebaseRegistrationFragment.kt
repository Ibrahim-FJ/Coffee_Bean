package com.ibrahimf.coffeebean.regiseration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.databinding.FragmentFirebaseRegiserationBinding


class FirebaseRegistrationFragment : Fragment() {


    val signInLauncher = registerForActivityResult(
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
        signInLauncher.launch(signInIntent)
    }


    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            findNavController().navigateUp()


        } else {
            println("none")
        }
    }

}