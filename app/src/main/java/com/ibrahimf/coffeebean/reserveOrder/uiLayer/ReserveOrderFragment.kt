package com.ibrahimf.coffeebean.reserveOrder.uiLayer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.ibrahimf.coffeebean.R
import com.ibrahimf.coffeebean.databinding.FragmentReserveOrderBinding
import com.ibrahimf.coffeebean.reserveOrder.dataLayer.Order
import com.ibrahimf.coffeebean.util.ViewModelFactory


class ReserveOrderFragment : Fragment() {
    private var binding: FragmentReserveOrderBinding? = null
    private val reserveOrderViewModel: ReserveOrderViewModel by activityViewModels{
        ViewModelFactory()
    }
    private val navigationArgs: ReserveOrderFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentReserveOrderBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding?.apply {
            confirmReservation.setOnClickListener {
               val quantity =  quantityEditText.text.toString()
                val messageToSeller =  messageToSellerEditText.text.toString()

                if (quantity.isNotEmpty() && messageToSeller.isNotEmpty()){
                    showConfirmationDialog(quantity, messageToSeller)

                }else{
                    Toast.makeText(this@ReserveOrderFragment.requireContext(), "complete the fields", Toast.LENGTH_SHORT).show()

                }

            }

        }

    }


    fun reserveOrder(quantity: String, messageToSeller: String){

        if (reserveOrderViewModel.reserveOrder(Order(navigationArgs.productID, quantity, messageToSeller, navigationArgs.sellerId))){
            Toast.makeText(this@ReserveOrderFragment.requireContext(), "order complete", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_reserveOrderFragment_to_productListFragment)
        }else{
            Toast.makeText(this@ReserveOrderFragment.requireContext(), "you can't order from this seller", Toast.LENGTH_SHORT).show()
        }



    }

    /**
     * Displays an alert dialog to get the user's confirmation.
     */
    private fun showConfirmationDialog(quantity: String, messageToSeller: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(android.R.string.dialog_alert_title))
            .setMessage(getString(R.string.reserve_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.no)) { _, _ ->
            }
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                reserveOrder(quantity, messageToSeller)
            }
            .show()
    }
}