// SearchStockAdapter.kt
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.stocktracker.Stock
import com.example.stocktracker.StockViewModel
import com.example.stocktracker.databinding.SearchStockListItemBinding

class SearchStockAdapter(private val viewModel: StockViewModel) :
    ListAdapter<Stock, SearchStockAdapter.SearchStockViewHolder>(StockDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchStockViewHolder {
        val binding = SearchStockListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchStockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchStockViewHolder, position: Int) {
        val stock = getItem(position)
        holder.bind(stock)
    }

    inner class SearchStockViewHolder(private val binding: SearchStockListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(stock: Stock) {
            binding.searchStockSymbol.text = stock.symbol
            binding.searchStockName.text = stock.name

            binding.searchFollowButton.text = if (viewModel.isStockFollowed(stock.symbol)) "Unfollow" else "Follow"

            binding.searchFollowButton.setOnClickListener {
                if (!viewModel.isStockFollowed(stock.symbol)) {
                    viewModel.addStockToFollow(stock)
                    binding.searchFollowButton.text = "Unfollow"
                } else {
                    viewModel.removeStockFromFollow(stock.symbol)
                    binding.searchFollowButton.text = "Follow"
                }
            }
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