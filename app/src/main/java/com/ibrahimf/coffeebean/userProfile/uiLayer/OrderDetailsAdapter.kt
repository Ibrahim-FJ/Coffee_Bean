package com.ibrahimf.coffeebean.userProfile.uiLayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ibrahimf.coffeebean.databinding.OrderDetailsItemBinding


class OrderDetailsAdapter(private val context: Context, private val onClicked:(orderDetailsUiState: OrderDetailsUiState)-> Unit): ListAdapter<OrderDetailsUiState, OrderDetailsAdapter.OrderDetailsViewHolder>(
    DiffCallback
) {

    class OrderDetailsViewHolder(var binding: OrderDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(orderDetailsUiState: OrderDetailsUiState) {

            binding.apply {

                nameItem.text = orderDetailsUiState.name
                phoneItem.text = orderDetailsUiState.phone


            }

        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetailsViewHolder {
        return OrderDetailsViewHolder(
            OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }


    override fun onBindViewHolder(
        holder: OrderDetailsViewHolder,
        position: Int
    ) {
        val currentOrder = getItem(position)
        holder.bind(currentOrder)

        holder.binding.ordersItemCard.setOnClickListener {

            onClicked(currentOrder)
        }

        Glide.with(context).load(currentOrder.userImage).circleCrop().into(holder.binding.imageView)

    }


    companion object DiffCallback : DiffUtil.ItemCallback<OrderDetailsUiState>() {
        override fun areItemsTheSame(oldItem: OrderDetailsUiState, newItem: OrderDetailsUiState): Boolean {
            return oldItem.phone == newItem.phone
        }


        override fun areContentsTheSame(oldItem: OrderDetailsUiState, newItem: OrderDetailsUiState): Boolean {
            return oldItem.name == newItem.name
        }
    }

}

