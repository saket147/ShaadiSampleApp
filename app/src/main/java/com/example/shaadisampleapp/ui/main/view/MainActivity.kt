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
import com.example.shaadisampleapp.R
import com.example.shaadisampleapp.database.viewmodel.VideosTableViewModel
import com.example.shaadisampleapp.network.api.ApiHelper
import com.example.shaadisampleapp.network.api.RetrofitBuilder
import com.example.shaadisampleapp.network.model.Results
import com.example.shaadisampleapp.network.model.VideosApiResponse
import com.example.shaadisampleapp.ui.base.ViewModelFactory
import com.example.shaadisampleapp.ui.main.adapter.MainAdapter
import com.example.shaadisampleapp.ui.main.interfaces.VideoItemClickInterface
import com.example.shaadisampleapp.ui.main.viewmodel.MainViewModel
import com.example.shaadisampleapp.utils.ConnectionUtil
import com.example.shaadisampleapp.utils.Status
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), VideoItemClickInterface {
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MainAdapter
    private val uiScope = CoroutineScope(Dispatchers.Main)
    private var videosTableViewModel: VideosTableViewModel? = null
    private var videosApiResponse: VideosApiResponse? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupViewModel()
        setupUI()
        val connectionUtil: ConnectionUtil? = ConnectionUtil(this)

        if (connectionUtil?.isOnline() == true) {
            setupObservers()
        } else {
            progressBar?.visibility = View.GONE
            recyclerView?.visibility = View.GONE
            tvError?.visibility = View.VISIBLE
            tvError?.text = "No Internet connection!"
        }
        ConnectionUtil(this).onInternetStateListener(object :
            ConnectionUtil.ConnectionStateListener {
            override fun onAvailable(isAvailable: Boolean) {
                uiScope.launch {
                    if (isAvailable) {
                        if (videosApiResponse == null) {
                            setupObservers()
                        }
                    } else {
                        if (videosApiResponse == null) {
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
        videosTableViewModel =
                ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                    .create(VideosTableViewModel::class.java)
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
        viewModel.getVideos(results = 10)
            .observe(this, Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.SUCCESS -> {
                            recyclerView?.visibility = View.VISIBLE
                            progressBar?.visibility = View.GONE
                            tvError?.visibility = View.GONE
                            resource.data?.let { videosResponse: VideosApiResponse ->
                                this.videosApiResponse = videosResponse
                                retrieveList(
                                    videosResponse.results
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

    //click listener for recyclerview item
    override fun onItemClick(result: Results?) {
        uiScope.launch {
            videosTableViewModel?.insertItem(result)
            Toast.makeText(
                this@MainActivity,
                result?.nat + " is now saved in database",
                Toast.LENGTH_LONG
            ).show()
        }
        Log.d("onItemClick", "Clicked on Saved history item")
    }
}