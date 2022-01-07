package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.ibrahimf.coffeebean.addProduct.ui.ViewModelFactory
import com.ibrahimf.coffeebean.databinding.FragmentUserProfileBinding
import com.ibrahimf.coffeebean.showProducts.uiLayer.ProductsListAdapter
import kotlinx.android.synthetic.main.product_list_item.*


class UserProfileFragment : Fragment() {
    private var binding: FragmentUserProfileBinding? = null

    private val userProfileViewModel: UserProfileViewModel? by activityViewModels {
        ViewModelFactory()
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
        val ordersAdapter = ProductsListAdapter(this.requireContext()) {}
        val reservationRequestAdapter = ProductsListAdapter(this.requireContext()) {}
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


        userProfileViewModel?._userOrders?.observe(viewLifecycleOwner, {
            it.let {
                ordersAdapter.submitList(it)
            }
        })



        userProfileViewModel?._userReservationRequest?.observe(viewLifecycleOwner, {
            it.let {
                reservationRequestAdapter.submitList(it)
            }
        })

        userProfileViewModel?._userPosts?.observe(viewLifecycleOwner, {
            it.let {
                Log.e("TAG", "onViewCreatedPosts: $it", )
                userPostsAdapter.submitList(it)
            }
        })


    }
}