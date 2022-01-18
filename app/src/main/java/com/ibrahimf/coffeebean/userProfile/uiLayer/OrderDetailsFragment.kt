package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    val binding get() = _binding
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val navigationArgs: OrderDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        Glide.with(this.requireContext()).load(navigationArgs.imageUrl).into(binding?.imageUri!!)
        binding?.name?.text = navigationArgs.name
        binding?.phone?.text = navigationArgs.phone
        binding?.message?.text = navigationArgs.message
        binding?.quantity?.text = navigationArgs.quentity

    }

}