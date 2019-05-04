package com.rozedfrozzy.findgithubuser.ui

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rozedfrozzy.findgithubuser.R
import com.rozedfrozzy.findgithubuser.data.network.NetworkService
import com.rozedfrozzy.findgithubuser.data.network.SchedulersWrappers
import com.rozedfrozzy.findgithubuser.ui.adapter.UserItemsAdapter
import com.rozedfrozzy.findgithubuser.utils.InfiniteScrollListener
import kotlinx.android.synthetic.main.activity_home.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private var adapter = UserItemsAdapter(this)

    var currentPage = 0
    var searchKey: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        viewModel = ViewModelProviders.of(this, HomeViewModelFactory(SchedulersWrappers())).get(HomeViewModel::class.java)
        viewModel.networkService = NetworkService()

        initializedSearchView()
        configureRecyclerview()
        observableListener()
    }

    private fun observableListener() {
        viewModel.getResultListObservable().observe(this, Observer {
            adapter.updateList(it!!)
            hideProgressBar()
        })

        viewModel.getResultListErrorObservable().observe(this, Observer {
            longToast(getString(R.string.server_busy))
            hideProgressBar()
        })
    }

    private fun initializedSearchView() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchBar.clearFocus()
                return if (!isOnline()){
                    toast(getString(R.string.no_connection))
                    false
                } else {
                    showProgressBar()
                    adapter.clearData()
                    searchKey = query
                    currentPage = 1
                    /*viewModel.searchKey = query
                    viewModel.currentPage = 1*/
                    viewModel.searchAddress(query)
                    true
                }
            }

        })
    }

    @SuppressLint("WrongConstant")
    private fun configureRecyclerview() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerViewUserItems.apply {
            recyclerViewUserItems.layoutManager = layoutManager
            addOnScrollListener(InfiniteScrollListener({ loadData() }, layoutManager))
        }
        recyclerViewUserItems.adapter = adapter
    }

    fun loadData() {
        /*var currentPage = viewModel.currentPage
        var searchKey = viewModel.searchKey*/
        currentPage++
        viewModel.searchAddress(searchKey, currentPage)
        showProgressBar()
    }

    fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    private fun isOnline(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
