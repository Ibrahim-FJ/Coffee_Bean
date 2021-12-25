package com.ibrahimf.coffeebean

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.ibrahimf.coffeebean.databinding.FragmentAddProductBinding


class AddProductFragment : Fragment() {
    private var binding: FragmentAddProductBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddProductBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.addImagesLayout?.setOnClickListener {
           // startActivity(Intent(this.requireContext(), CameraActivity::class.java))
            findNavController().navigate(R.id.action_addProductFragment_to_cameraFragment2)

        }
    }

}