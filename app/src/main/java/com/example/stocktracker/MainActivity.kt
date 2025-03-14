package com.example.stocktracker

import SearchStockAdapter
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.stocktracker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var searchAdapter: SearchStockAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StockViewModel
    private lateinit var adapter: StockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = Room.databaseBuilder(applicationContext, StockDatabase::class.java, "stock_database").build()
        val repository = StockRepository(database.stockDao())
        viewModel = ViewModelProvider(this, StockViewModelFactory(repository))[StockViewModel::class.java]

        adapter = StockAdapter(viewModel)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
        searchAdapter = SearchStockAdapter(viewModel)

        viewModel.followedStocks.observe(this) { stocks ->
            if (!isSearchActive) {
                adapter.submitList(stocks)
            }
        }

        viewModel.searchResults.observe(this) { searchResults ->
            if (isSearchActive) {
                searchAdapter.submitList(searchResults)
            }
        }

        viewModel.loadFollowedStocks()
    }

    private var isSearchActive = false

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d("MainActivity", "Search query: $query")
                if (!query.isNullOrEmpty()) {
                    viewModel.searchStocks(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // You can add real-time searching here if needed
                return true
            }
        })

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                isSearchActive = true
                binding.recyclerView.adapter = searchAdapter
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                isSearchActive = false
                binding.recyclerView.adapter = adapter
                viewModel.loadFollowedStocks() // Reload followed stocks
                return true
            }
        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> true // Handled in onCreateOptionsMenu
            else -> super.onOptionsItemSelected(item)
        }
    }

}