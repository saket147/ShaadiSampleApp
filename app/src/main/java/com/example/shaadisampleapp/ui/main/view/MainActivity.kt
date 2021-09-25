package com.example.shaadisampleapp.ui.main.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.shaadisampleapp.R
import com.example.shaadisampleapp.database.viewmodel.MatchesTableViewModel
import com.example.shaadisampleapp.network.api.ApiHelper
import com.example.shaadisampleapp.network.api.RetrofitBuilder
import com.example.shaadisampleapp.network.model.MatchesApiResponse
import com.example.shaadisampleapp.network.model.Results
import com.example.shaadisampleapp.ui.base.ViewModelFactory
import com.example.shaadisampleapp.ui.main.adapter.MainAdapter
import com.example.shaadisampleapp.ui.main.interfaces.MatchesItemClickInterface
import com.example.shaadisampleapp.ui.main.viewmodel.MainViewModel
import com.example.shaadisampleapp.utils.ConnectionUtil
import com.example.shaadisampleapp.utils.Constants
import com.example.shaadisampleapp.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MatchesItemClickInterface {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var matchesTableViewModel: MatchesTableViewModel? = null
    private var matchesApiResponse: MatchesApiResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()

        val connectionUtil: ConnectionUtil? = ConnectionUtil(this)
        swipeRefresh?.isRefreshing = false
        swipeRefresh?.setOnRefreshListener {
            if (connectionUtil?.isOnline() == true) {
                setupObservers()
            } else {
                if (matchesApiResponse == null || matchesApiResponse?.results?.isEmpty() == true) {
                    progressBar?.visibility = View.GONE
                    recyclerView?.visibility = View.GONE
                    tvError?.visibility = View.VISIBLE
                    tvError?.text = "No Internet connection!"
                }
                Toast.makeText(this, "No Internet connection!", Toast.LENGTH_SHORT).show()
                swipeRefresh?.isRefreshing = false
            }
        }

        if (connectionUtil?.isOnline() == true) {
            setupObservers()
        } else {
            try {
                uiScope.launch {
                    matchesTableViewModel?.savedMatches?.collect { it ->
                        if (it != null && it.isNotEmpty()) {
                            matchesApiResponse = MatchesApiResponse(it as MutableList<Results?>?)
                            recyclerView?.visibility = View.VISIBLE
                            progressBar?.visibility = View.GONE
                            tvError?.visibility = View.GONE
                            retrieveList(
                                matchesApiResponse?.results
                            )
                        } else {
                            progressBar?.visibility = View.GONE
                            recyclerView?.visibility = View.GONE
                            tvError?.visibility = View.VISIBLE
                            tvError?.text = "No Internet connection!"
                        }
                    }
                }
            } catch (ex: Exception) {
                progressBar?.visibility = View.GONE
                recyclerView?.visibility = View.GONE
                tvError?.visibility = View.VISIBLE
                tvError?.text = "No Internet connection!"
            }
        }

        ConnectionUtil(this).onInternetStateListener(object :
            ConnectionUtil.ConnectionStateListener {
            override fun onAvailable(isAvailable: Boolean) {
                uiScope.launch {
                    if (isAvailable) {
                        if (matchesApiResponse == null || matchesApiResponse?.results?.isEmpty() == true) {
                            setupObservers()
                        }
                    } else {
                        if (matchesApiResponse == null || matchesApiResponse?.results?.isEmpty() == true) {
                            progressBar?.visibility = View.GONE
                            recyclerView?.visibility = View.GONE
                            tvError?.visibility = View.VISIBLE
                            tvError?.text = "No Internet connection!"
                        }
                    }
                }
            }
        })
    }

    private fun setupViewModel() {
        viewModel =
            ViewModelProvider(this, ViewModelFactory(ApiHelper(RetrofitBuilder.apiService))).get(
                MainViewModel::class.java
            )
        matchesTableViewModel =
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                .create(MatchesTableViewModel::class.java)
    }

    //initialising the adapter
    private fun setupUI() {
        recyclerView?.layoutManager = LinearLayoutManager(this)
        adapter = MainAdapter(arrayListOf(), this)
        recyclerView?.addItemDecoration(
            DividerItemDecoration(
                recyclerView?.context,
                (recyclerView?.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView?.adapter = adapter
    }

    //observing the response from the api
    private fun setupObservers() {
        swipeRefresh?.isEnabled = false
        viewModel.getMatches(results = 10)
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            recyclerView?.visibility = View.VISIBLE
                            progressBar?.visibility = View.GONE
                            tvError?.visibility = View.GONE
                            resource.data?.let { it ->
                                this.matchesApiResponse = it
                                uiScope.launch {
                                    matchesTableViewModel?.nukeTable(SimpleSQLiteQuery("VACUUM"))
                                    matchesTableViewModel?.insertAllItem(matchesApiResponse?.results as List<Results?>?)
                                }
                                retrieveList(
                                    matchesApiResponse?.results
                                )
                            }
                        }
                        Status.ERROR -> {
                            recyclerView?.visibility = View.VISIBLE
                            progressBar?.visibility = View.GONE
                            tvError?.visibility = View.VISIBLE
                            tvError?.text = it.message
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                        Status.LOADING -> {
                            progressBar?.visibility = View.VISIBLE
                            recyclerView?.visibility = View.GONE
                            tvError?.visibility = View.GONE
                        }
                    }
                }
                swipeRefresh?.isRefreshing = false
                swipeRefresh?.isEnabled = true
            })
    }

    //notifying adapter changes
    //sending data to adapter class
    private fun retrieveList(results: Collection<Results?>?) {
        adapter.apply {
            addUsers(results)
            notifyDataSetChanged()
        }
    }

    override fun onMatchStatusChange(result: Results?, status: String?, pos: Int) {
        uiScope.launch {
            matchesTableViewModel?.updateMatchStatus(result?.email, status)
            result?.matchStatus = status
            adapter.notifyItemChanged(pos, result)
            when(status){
                Constants.PROFILE_ACCEPTED -> {
                    Toast.makeText(
                        this@MainActivity, "You have accepted ${result?.name?.first}'s request",
                        Toast.LENGTH_LONG
                    ).show()
                }
                Constants.PROFILE_DECLINED -> {
                    Toast.makeText(
                        this@MainActivity, "You have declined ${result?.name?.first}'s request",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        Log.d("onMatchStatusChange", "Clicked on Saved history item  $status")
    }
}