package com.example.mypaging.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mypaging.R
import com.example.mypaging.databinding.ActivityMainBinding
import com.example.mypaging.main.data.NetworkState
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainView {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        binding.viewModel = viewModel

        // setup list adapter/controller
//        binding.rvPosts.layoutManager = LinearLayoutManager(this)
//        binding.rvPosts.adapter = ArticleAdapter { viewModel.retry() }
        val controller = ArticleController { viewModel.retry() }
        binding.rvPosts.setController(controller)
        Timber.d("MainActivity onCreate")
        viewModel.articleLivedata.observe(this, Observer {
            Timber.d("observe list, size = ${it.size}")
            controller.submitList(it)
//            (binding.rvPosts.adapter as ArticleAdapter).submitList(it)
        })
        viewModel.networkState.observe(this, Observer {
            Timber.d("observe networkState, it = $it")
            controller.networkState = it
//            (binding.rvPosts.adapter as ArticleAdapter).setNetworkState(it)
        })
        viewModel.refreshState.observe(this, Observer {
            Timber.d("observe refreshState, it = $it")
            binding.swipeRefresh.isRefreshing = (it == NetworkState.LOADING)
        })

        binding.swipeRefresh.setOnRefreshListener { viewModel.refresh() }
    }
}
