package com.ibrahimf.coffeebean.showProducts.uiLayer

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.addProduct.ui.ViewModelFactory
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
//        productListViewModel.getAllProducts()
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

        val adapter = ProductsListAdapter(this.requireContext())
        binding?.productsRecyclerView?.adapter = adapter




//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                productListViewModel.productsStateFlow.collect {
//                    adapter.submitList(it)
//                }
//            }
//        }


        productListViewModel.productStatFlowToLiveData.observe(viewLifecycleOwner, {
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
                showConfirmationDialog()
            }
            R.id.add_product ->{
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

    override fun onResume() {
        super.onResume()

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

    /**
     * Displays an alert dialog to get the user's confirmation before deleting the item.
     */
    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ -> }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                signOut()
                isSignedIn = false

            }
            .show()
    }

}