package com.ibrahimf.coffeebean

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.addProduct.ui.AddProductViewModel
import com.ibrahimf.coffeebean.addProduct.ui.ProductsListAdapter
import com.ibrahimf.coffeebean.addProduct.ui.ProductsListViewModel
import com.ibrahimf.coffeebean.addProduct.ui.ViewModelFactory
import com.ibrahimf.coffeebean.databinding.FragmentPhoneImagesBinding
import com.ibrahimf.coffeebean.databinding.FragmentProductListBinding


class ProductListFragment : Fragment() {
    var isSignedIn = false

    private val productListViewModel: ProductsListViewModel by activityViewModels {
        ViewModelFactory()
    }

    private var _binding:FragmentProductListBinding ?= null
    val binding get() = _binding

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        productListViewModel.getAllProducts()
        val adapter = ProductsListAdapter(this.requireContext())
        binding?.productsRecyclerView?.adapter = adapter
        productListViewModel.products.value.observe(viewLifecycleOwner, {
            it.let {
                adapter.submitList(it)

            }
        })


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
            R.id.add_product ->{
                println("Ibrahim")


                findNavController().navigate(R.id.action_productListFragment_to_addProductFragment)
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