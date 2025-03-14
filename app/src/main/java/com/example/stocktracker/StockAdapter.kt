package com.example.stocktracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stocktracker.databinding.StockListItemBinding

class StockAdapter(private val viewModel: StockViewModel) :
    ListAdapter<Stock, StockAdapter.StockViewHolder>(StockDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val binding = StockListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = getItem(position)
        holder.bind(stock)
    }

    inner class StockViewHolder(private val binding: StockListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: Stock) {
            binding.stockSymbol.text = stock.symbol
            binding.stockName.text = stock.name
            binding.stockPrice.text = stock.price?.toString() ?: "Loading..."

            binding.followButton.setOnClickListener {
                if (currentList.contains(stock)) {
                    viewModel.removeStockFromFollow(stock.symbol)
                } else {
                    viewModel.addStockToFollow(stock)
                }
            }
            binding.followButton.text = if (currentList.contains(stock)) "Unfollow" else "Follow"
        }
    }

    class StockDiffCallback : DiffUtil.ItemCallback<Stock>() {
        override fun areItemsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem.symbol == newItem.symbol
        }

        override fun areContentsTheSame(oldItem: Stock, newItem: Stock): Boolean {
            return oldItem == newItem
        }
    }
}