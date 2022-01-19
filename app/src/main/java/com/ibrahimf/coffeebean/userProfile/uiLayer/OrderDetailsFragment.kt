package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.databinding.FragmentOrderDetailsBinding
import java.lang.Exception

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    val binding get() = _binding
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

        try {
            Glide.with(this.requireContext()).load(navigationArgs.imageUrl).circleCrop().into(binding?.imageUri!!)
            binding?.name?.text = navigationArgs.name
            binding?.phone?.text = navigationArgs.phone
            binding?.message?.text = navigationArgs.message
            binding?.quantity?.text = navigationArgs.quentity
        }catch (e: Exception){
            println(e.message)
        }

        binding?.phone?.setOnClickListener {
            openDial(binding?.phone?.text.toString())
        }

    }


    // This function is called when button is clicked.
    fun openDial(phoneNumber: String){
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
        }catch (e: SecurityException){
            Toast.makeText(this.requireContext(), "An error occurred", Toast.LENGTH_LONG).show()
        }
    }

}