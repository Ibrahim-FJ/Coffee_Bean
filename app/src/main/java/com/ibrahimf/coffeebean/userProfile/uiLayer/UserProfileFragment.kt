package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.databinding.FragmentUserProfileBinding
import com.ibrahimf.coffeebean.showProducts.uiLayer.ProductListFragment
import com.ibrahimf.coffeebean.showProducts.uiLayer.ProductsListAdapter
import com.ibrahimf.coffeebean.util.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_user_registration.*


class
UserProfileFragment : Fragment() {
    private var binding: FragmentUserProfileBinding? = null

    private val userProfileViewModel: UserProfileViewModel? by activityViewModels {
        ViewModelFactory()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userProfileViewModel?.getUserOrders()
        userProfileViewModel?.getUserPosts()
        userProfileViewModel?.getUserReservationRequest()

        val ordersAdapter = ProductsListAdapter(this.requireContext()) {}
        val reservationRequestAdapter = OrderDetailsAdapter(this.requireContext()){
            val action = UserProfileFragmentDirections.actionUserProfileFragmentToOrderDetailsFragment(
             imageUrl = it.userImage, name = it.name, phone = it.phone, message = it.message, quentity = it.quantity
            )
            findNavController().navigate(action)
        }
        val userPostsAdapter = ProductsListAdapter(this.requireContext()){
            val action = UserProfileFragmentDirections.actionUserProfileFragmentToEditProductFragment(
                productTitle = it.title, productDetails = it.details, imagesList = it.imageUri.toTypedArray(), productID = it.productID
            )
            findNavController().navigate(action)
        }

        binding?.ordersRecyclerView?.adapter = ordersAdapter
        binding?.reservationRecyclerView?.adapter = reservationRequestAdapter
        binding?.postsRecyclerView?.adapter = userPostsAdapter

//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.RESUMED) {
//                userProfileViewModel?.ordersStateFlow?.collect {
//                    adapter.submitList(it)
//                    Log.e("TAG", "onViewCreated: ${it}")
//                }
//            }
//        }

        userProfileViewModel?.userOrders?.observe(viewLifecycleOwner, {
            it.let {
                ordersAdapter.submitList(it)
            }
        })

        userProfileViewModel?._userReservationRequest?.observe(viewLifecycleOwner, {
            it.let {
                reservationRequestAdapter.submitList(it)
            }

        })

//        userProfileViewModel?._userReservationRequest?.observe(viewLifecycleOwner, {
//            it.let {
//                reservationRequestAdapter.submitList(it)
//            }
//        })

        userProfileViewModel?._userPosts?.observe(viewLifecycleOwner, {
            it.let {
                userPostsAdapter.submitList(it)
            }
        })

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.user_profile_menu, menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.user_profile ->{
                findNavController().navigate(R.id.action_userProfileFragment_to_userRegistrationFragment)
            }
        }
        return true
    }

}